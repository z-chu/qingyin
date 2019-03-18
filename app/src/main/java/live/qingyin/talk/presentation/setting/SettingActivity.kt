package live.qingyin.talk.presentation.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }

    }
}