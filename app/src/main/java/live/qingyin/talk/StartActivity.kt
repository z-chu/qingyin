package live.qingyin.talk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.zchu.bridge._subscribe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_start.*
import java.util.concurrent.TimeUnit

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        startActivity(Intent(this, MainActivity::class.java))
        stateful_view.onContentViewCreatedListener = {

        }
        stateful_view.onErrorViewCreatedListener = {

        }
        stateful_view.onLoadingViewCreatedListener = {

        }
        stateful_view.onRetryListener = {
            loadData()
        }

        loadData()
    }

    fun loadData() {
        stateful_view.showLoading("加载中")
        Observable.just(2)
            .flatMap {
                if (System.currentTimeMillis() % it == 0L) {
                    Observable.just(it)
                } else {
                    Observable.error(RuntimeException("哇，出bug啦"))
                }
            }

            .delay(4, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            ._subscribe {
                _onError {
                    stateful_view.showError(it.message)
                }
                _onNext {
                    stateful_view.showContent()
                }
            }
    }
}
