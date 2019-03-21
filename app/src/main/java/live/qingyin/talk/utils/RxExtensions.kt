package live.qingyin.talk.utils

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.zchu.model.ViewData
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.asLiveDataOfViewData(): ResourceLiveData<T> {
    return ResourceLiveData(this)
}

@CheckReturnValue
fun <T> Observable<T>.subscribe(mutableLiveData: MutableLiveData<ViewData<T>>): Disposable {
    mutableLiveData.safeSetValue(ViewData.loading())
    return this.subscribe({
        mutableLiveData.safeSetValue(ViewData.loaded(it))

    }, {
        mutableLiveData.safeSetValue(ViewData.error(it))
    })
}

fun <T> MutableLiveData<T>.safeSetValue(value: T) {
    if (Looper.getMainLooper().thread === Thread.currentThread()) {
        setValue(value)
    } else {
        postValue(value)
    }
}

class ResourceLiveData<T>(private val observable: Observable<T>) : LiveData<ViewData<T>>(), Disposable {

    @Volatile
    private var disposable: Disposable? = null

    private var isDisposed: Boolean = false
    private var isSubscribed: Boolean = false

    override fun isDisposed(): Boolean {
        return disposable?.isDisposed ?: isDisposed
    }

    override fun dispose() {
        disposable?.dispose()
        isDisposed = true
    }

    private fun subscribe() {
        if (!isDisposed) {
            observable
                .subscribe(object : Observer<T> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        safeSetValue(ViewData.loading())
                    }

                    override fun onNext(t: T) {
                        safeSetValue(ViewData.loaded(t))
                    }

                    override fun onError(e: Throwable) {
                        safeSetValue(ViewData.error(e))
                    }

                    override fun onComplete() {
                    }

                })
        }
    }


    private fun safeSetValue(value: ViewData<T>) {
        if (Looper.getMainLooper().thread === Thread.currentThread()) {
            setValue(value)
        } else {
            postValue(value)
        }
    }


    override fun onActive() {
        super.onActive()
        if (!isSubscribed) {
            subscribe()
            isSubscribed = true
        }
    }


}