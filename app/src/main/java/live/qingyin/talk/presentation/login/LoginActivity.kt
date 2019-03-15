package live.qingyin.talk.presentation.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gelitenight.waveview.library.WaveView
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var mWaveHelper: WaveHelper

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            super.onFragmentDetached(fm, f)
            if (f is LoginDialogFragment) {
                finish()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        val supportFragmentManager = supportFragmentManager
        val dialog = supportFragmentManager.findFragmentByTag("dialog")
        if (dialog !is LoginDialogFragment) {
            val loginDialogFragment = LoginDialogFragment()
            loginDialogFragment.show(supportFragmentManager, "dialog")
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, false)
    }


    private fun initView() {
        val waveView = findViewById<WaveView>(R.id.wave)
        waveView.waterLevelRatio = 0.5f
        waveView.setShapeType(WaveView.ShapeType.SQUARE)
        waveView.setBorder(0, 0)
        waveView.setWaveColor(
            Color.parseColor("#28e6ee9c"),
            Color.parseColor("#3ce6ee9c")
        )
        mWaveHelper = WaveHelper(waveView)
        mWaveHelper.start()
    }


    override fun onResume() {
        super.onResume()
        mWaveHelper.start()


    }

    override fun onPause() {
        super.onPause()
        mWaveHelper.cancel()

    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        super.onDestroy()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}