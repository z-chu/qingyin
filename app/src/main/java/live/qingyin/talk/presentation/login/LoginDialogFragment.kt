package live.qingyin.talk.presentation.login

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.util.DebounceOnClickLister
import com.github.zchu.model.whenRun
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.login_card.*
import live.qingyin.talk.R
import live.qingyin.talk.presentation.main.MainActivity
import live.qingyin.talk.utils.getEasyMessage
import org.koin.android.viewmodel.ext.viewModel

class LoginDialogFragment : DialogFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel  by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LoginDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_sign_in.setOnClickListener(DebounceOnClickLister(this))
        viewModel
            .result()
            .observe(this, Observer {
                it.whenRun {
                    onLoading {
                        showLoading()
                    }
                    onSuccess {
                        showContent()
                    }
                    onError { throwable, _ ->
                        throwable?.let {
                            Logger.e(it, "login")
                        }
                        showError(throwable.getEasyMessage(context))
                    }
                }
            })
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_sign_in -> login()
        }
    }

    private fun login() {
        val username = et_username.text.toString()
        if (username.isBlank()) {
            et_username.error = "用户名不能为空"
            return
        }
        val password = et_password.text!!.toString()
        if (password.isBlank()) {
            et_password.error = "密码不能为空"
            return
        }
        if (password.length < 6) {
            et_password.error = "请输入6到16位密码"
            return
        }
        viewModel.login(username, password)
    }


    fun showUsernameWrong() {
        et_username.isEnabled = true
        et_password.isEnabled = true
        et_username.error = "用户名不存在"
        btn_sign_in.progress = 0
    }

    fun showPasswordWrong() {
        et_username.isEnabled = true
        et_password.isEnabled = true
        et_password.error = "密码不正确"
        btn_sign_in.progress = 0
    }


    fun showContent() {
        et_username.isEnabled = true
        et_password.isEnabled = true
        btn_sign_in.progress = 100
        MainActivity.start(requireContext())
        dismissAllowingStateLoss()

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }


    fun showLoading() {
        et_username.isEnabled = false
        et_password.isEnabled = false
        btn_sign_in.progress = 50
    }

    fun showError(errorMsg: String) {
        et_username.isEnabled = true
        et_password.isEnabled = true
        btn_sign_in.progress = 0
        activity?.showToastShort(errorMsg)
    }
}
