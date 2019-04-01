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
import kotlinx.android.synthetic.main.activity_modify_profile_name.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity

class NameModifyActivity : BaseActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_profile_name)
        initToolbar("修改昵称")
        val etName = et_profile_name
        etName.setText(intent?.getStringExtra("name"))
        etName.setSelection(etName.length())
        val maxLength = resources.getInteger(R.integer.max_length_profile_name)
        etName._addTextChangedListener {
            _onTextChanged { _, _, _, _ ->
                tv_text_size.text = "${etName.length()}/$maxLength"
            }
        }
        tv_text_size.text = "${etName.length()}/$maxLength"

        etName.setOnEditorActionListener { _, actionId, _ ->
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
            Intent().putExtra("profile_name", et_profile_name.text.toString())
        )
        finish()
    }


    companion object {
        fun newIntent(context: Context, oldName: String?): Intent {
            val intent = Intent(context, NameModifyActivity::class.java)
            intent.putExtra("name", oldName)
            return intent
        }

        fun getProfileName(intent: Intent?): String? {
            return intent?.getStringExtra("profile_name")
        }
    }
}