package live.qingyin.talk.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.zchu.common.help.showToastShort
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import live.qingyin.talk.R
import live.qingyin.talk.presentation.login.LoginActivity
import live.qingyin.talk.user.UserManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val userManager: UserManager by inject()


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showToastShort(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_following -> {
                return@OnNavigationItemSelectedListener checkLoggedIn {
                    showToastShort(R.string.title_following)
                }
            }
            R.id.navigation_messages -> {
                return@OnNavigationItemSelectedListener checkLoggedIn {
                    showToastShort(R.string.title_messages)
                }
            }
            R.id.navigation_me -> {
                LoginActivity.start(this)
                return@OnNavigationItemSelectedListener checkLoggedIn {
                    showToastShort(R.string.title_me)
                }
            }
        }
        false
    }

    private inline fun checkLoggedIn(block: () -> Unit): Boolean {
        return if (userManager.isLoggedIn()) {
            block.invoke()
            true
        } else {
            LoginActivity.start(this)
            false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        userManager
            .liveDataOfUser()
            .observe(this, Observer {
                it?.let {
                    tv_message.text = it.toString()
                }
            })

    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
