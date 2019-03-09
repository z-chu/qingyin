package live.qingyin.talk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.github.zchu.bridge._subscribe
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.rx.bindLifecycle
import com.github.zchu.common.util.argument
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val str: String by argument("test")

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_following -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_messages -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_me -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)



    }

    override fun onDestroy() {
        super.onDestroy()
        Observable.just(str)
            .delay(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            ._subscribe {
                _onNext {
                    showToastShort(it)
                }
            }
            .bindLifecycle(this, Lifecycle.Event.ON_PAUSE)
    }
}
