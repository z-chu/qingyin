package live.qingyin.talk.presentation.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.zchu.common.help.initToolbar
import com.github.zchu.common.util.setDebounceOnClickLister
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_setting.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity
import live.qingyin.talk.presentation.main.MainActivity
import live.qingyin.talk.usersession.UserSessionManager
import org.koin.android.ext.android.inject


class SettingActivity : BaseActivity() {

    private val userSessionManager: UserSessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(live.qingyin.talk.R.layout.activity_setting)
        initToolbar(getString(R.string.title_setting))
        row_logout.setDebounceOnClickLister {
            showSignOutWarnDialog()
        }

    }

    private fun showSignOutWarnDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.action_sign_out)
            .setMessage(R.string.message_warn_sign_out)
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                signOut()
            }
            .show()
    }

    private fun signOut() {
        userSessionManager.signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }

    }
}