package live.qingyin.talk.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zchu.common.rx.schedule4Io2Main
import com.github.zchu.model.ViewData
import live.qingyin.talk.data.json.toUser
import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.utils.subscribe

class LoginViewModel(
    private val userRepository: UserRepository
    , private val userManager: live.qingyin.talk.usersession.UserManager
) : ViewModel() {

    private val viewData: MutableLiveData<ViewData<live.qingyin.talk.usersession.model.User>> by lazy {
        MutableLiveData<ViewData<live.qingyin.talk.usersession.model.User>>()
    }

    fun result(): LiveData<ViewData<live.qingyin.talk.usersession.model.User>> {
        return viewData
    }

    fun login(username: String, password: String) {
        userRepository
            .loginOrRegister(username, password)
            .map {
                it.toUser()
            }
            .schedule4Io2Main()
            .doOnNext {
                userManager.saveUser(it)
            }
            .subscribe(viewData)
    }

}

