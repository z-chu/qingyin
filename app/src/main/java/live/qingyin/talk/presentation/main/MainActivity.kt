package live.qingyin.talk.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.zchu.common.util.selectFragmentDisplay
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity
import live.qingyin.talk.presentation.follow.FollowingFragment
import live.qingyin.talk.presentation.home.HomeFragment
import live.qingyin.talk.presentation.login.LoginActivity
import live.qingyin.talk.presentation.messages.MessagesFragment
import live.qingyin.talk.presentation.user.MeFragment
import live.qingyin.talk.usersession.UserSessionManager
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val userManager: UserSessionManager by inject()



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val itemId = item.itemId
        val supportFragmentManager = supportFragmentManager
        when (itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.selectFragmentDisplay(containerViewId, itemId.toString(), fragmentFunc)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_following,
            R.id.navigation_messages,
            R.id.navigation_me -> {
                return@OnNavigationItemSelectedListener checkLoggedIn {
                    supportFragmentManager.selectFragmentDisplay(containerViewId, itemId.toString(), fragmentFunc)
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
        supportFragmentManager.selectFragmentDisplay(containerViewId, R.id.navigation_home.toString(), fragmentFunc)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    companion object {
        private const val containerViewId = R.id.container
        private val fragmentFunc: (tag: String) -> Fragment = {
            val tagId = it.toInt()
            when (tagId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_following -> FollowingFragment()
                R.id.navigation_messages -> MessagesFragment()
                R.id.navigation_me -> MeFragment()
                else -> error("没有给 fragment tag：$it 返回对应的 Fragment 实例。")
            }
        }

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
