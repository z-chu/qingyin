package live.qingyin.talk.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.zchu.common.rx.RxViewModel
import com.github.zchu.common.rx.schedule4Io2Main
import com.github.zchu.model.WorkResult
import live.qingyin.talk.data.json.toUserSession
import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.usersession.UserSessionManager
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.subscribe

class LoginViewModel(
    private val userRepository: UserRepository
    , private val userManager: UserSessionManager
) : RxViewModel() {

    private val viewData: MutableLiveData<WorkResult<UserSession>> by lazy {
        MutableLiveData<WorkResult<UserSession>>()
    }

    fun result(): LiveData<WorkResult<UserSession>> {
        return viewData
    }

    fun login(username: String, password: String) {
        userRepository
            .loginOrRegister(username, password)
            .map {
                it.toUserSession()
            }
            .schedule4Io2Main()
            .doOnNext {
                userManager.saveUser(it)
            }
            .subscribe(viewData)
            .disposeWhenCleared()
    }

}

