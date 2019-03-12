package live.qingyin.talk.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zchu.common.rx.schedule4Io2Main
import com.github.zchu.model.ViewData
import live.qingyin.talk.data.json.toUser
import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.user.UserManager
import live.qingyin.talk.user.model.User
import live.qingyin.talk.utils.subscribe

class LoginViewModel(
    private val userRepository: UserRepository
    , private val userManager: UserManager
) : ViewModel() {

    private val viewData: MutableLiveData<ViewData<User>> by lazy {
        MutableLiveData<ViewData<User>>()
    }

    fun result(): LiveData<ViewData<User>> {
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

