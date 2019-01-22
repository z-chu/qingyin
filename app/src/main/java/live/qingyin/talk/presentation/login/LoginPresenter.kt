package live.qingyin.talk.presentation.login

import com.github.zchu.common.help.toastShort
import com.github.zchu.mvp.SuperPresenter

class LoginPresenter(view: LoginActivity) : SuperPresenter<LoginActivity>(view) {
    override fun onViewInitialized() {
        super.onViewInitialized()
        runOnViewNonNull {
            it.toastShort("柱子哥牛逼了啊")
        }
    }

    override fun onStart() {
        super.onStart()
        view?.toastShort("onStart")
    }
}