package live.qingyin.talk.presentation.login

import com.github.zchu.common.help.showToastShort
import com.github.zchu.mvp.SuperPresenter

class LoginPresenter(view: LoginActivity) : SuperPresenter<LoginActivity>(view, view) {
    override fun onViewInitialized() {
        super.onViewInitialized()
        runOnViewNonNull {
            it.showToastShort("柱子哥牛逼了啊")
        }
    }

    override fun onStart() {
        super.onStart()
        view?.showToastShort("onStart")
    }
}