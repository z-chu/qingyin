package live.qingyin.talk

import android.content.Intent
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
import live.qingyin.talk.presentation.login.LoginActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val str: String by argument("test")

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        showToastShort(item.itemId.toString())
        //ToastDef.showLong(item.itemId.toString(), appContext())
        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
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
