package live.qingyin.talk.presentation.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import com.github.zchu.bridge._addTextChangedListener
import com.github.zchu.common.help.initToolbar
import kotlinx.android.synthetic.main.activity_modify_profile_bio.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity

class BioModifyActivity : BaseActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_profile_bio)
        initToolbar("修改个性签名")
        val etBio = et_profile_bio
        etBio.setText(intent?.getStringExtra("bio"))
        etBio.setSelection(etBio.length())
        val maxLength = resources.getInteger(R.integer.max_length_profile_bio)
        etBio._addTextChangedListener {
            _onTextChanged { _, _, _, _ ->
                tv_text_size.text = "${etBio.length()}/$maxLength"
            }
        }
        tv_text_size.text = "${etBio.length()}/$maxLength"

        etBio.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                done()
            }
            true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.action_done, menu)
        menu?.let {
            it.getItem(0).setOnMenuItemClickListener {
                done()
                return@setOnMenuItemClickListener true
            }
        }
        return true
    }

    private fun done() {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra("profile_bio", et_profile_bio.text.toString())
        )
        finish()
    }


    companion object {
        fun newIntent(context: Context, oldBio: String?): Intent {
            val intent = Intent(context, BioModifyActivity::class.java)
            intent.putExtra("bio", oldBio)
            return intent
        }

        fun getProfileBio(intent: Intent?): String? {
            return intent?.getStringExtra("profile_bio")
        }
    }
}