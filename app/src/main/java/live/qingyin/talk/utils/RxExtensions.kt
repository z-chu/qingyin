package live.qingyin.talk.utils

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.zchu.common.livedata.safeSetValue
import com.github.zchu.model.Failure
import com.github.zchu.model.Loading
import com.github.zchu.model.Success
import com.github.zchu.model.WorkResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.asLiveDataOfViewData(): ResourceLiveData<T> {
    return ResourceLiveData(this)
}

@CheckReturnValue
fun <T> Observable<T>.subscribe(mutableLiveData: MutableLiveData<WorkResult<T>>): Disposable {
    mutableLiveData.safeSetValue(Loading())
    return this.subscribe({
        mutableLiveData.safeSetValue(Success(it))

    }, {
        mutableLiveData.safeSetValue(Failure(it))
    })
}


class ResourceLiveData<T>(private val observable: Observable<T>) : LiveData<WorkResult<T>>(), Disposable {

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
                        safeSetValue(Loading())
                    }

                    override fun onNext(t: T) {
                        safeSetValue(Success(t))
                    }

                    override fun onError(e: Throwable) {
                        safeSetValue(Failure(e))
                    }

                    override fun onComplete() {
                    }

                })
        }
    }


    private fun safeSetValue(value: WorkResult<T>) {
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