package live.qingyin.talk.presentation.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.github.zchu.common.help.initToolbar
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity

class NameModifyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_profile_bio)
        initToolbar("修改昵称")


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_done, menu)
        return true
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NameModifyActivity::class.java))
        }
    }
}