package live.qingyin.talk.presentation.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.zchu.common.help.initToolbar
import live.qingyin.talk.base.ContainerActivity

class ProfileSettingsActivity : ContainerActivity() {

    override fun onCreated(savedInstanceState: Bundle?) {
        super.onCreated(savedInstanceState)
        initToolbar("个人资料设置")
    }

    override fun createFragment(): Fragment {
        return ProfileSettingsFragment()
    }


    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, ProfileSettingsActivity::class.java))
        }

    }

}