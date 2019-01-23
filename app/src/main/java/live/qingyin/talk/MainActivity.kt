package live.qingyin.talk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.rx._subscribe
import com.github.zchu.common.rx.bindLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import live.qingyin.talk.presentation.login.LoginActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        showToastShort(item.itemId.toString())
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        Observable.just("柱子哥牛逼")
            .delay(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            ._subscribe {
                _onNext {
                    showToastShort(it)
                }
            }
            .bindLifecycle(this)
    }
}
