package live.qingyin.talk.presentation.login

import com.github.zchu.common.help.showToastShort
import com.github.zchu.mvp.ComponentPresenter2
import com.github.zchu.mvp.attachAndDetachAt

class LoginPresenter(view: LoginActivity) : ComponentPresenter2<LoginActivity>(view, view) {

    init {
        attachAndDetachAt(view)
    }

    /*    override fun onViewInitialized() {
        super.onViewInitialized()
        runOnViewNonNull {
            it.showToastShort("柱子哥牛逼了啊")
        }
    }

    override fun attach(view: LoginActivity) {
        super.attach(view)

    }

    override fun onStart() {
        super.onStart()
        view?.showToastShort("onStart")
    }*/

    fun login() {
        view?.showToastShort("柱子哥牛逼了啊")
    }
}