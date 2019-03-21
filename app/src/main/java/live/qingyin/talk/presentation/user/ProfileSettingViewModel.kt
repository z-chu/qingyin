package live.qingyin.talk.presentation.user

import com.github.zchu.bridge._subscribe
import com.github.zchu.common.rx.RxViewModel
import com.github.zchu.common.rx.schedule4Io2Main
import live.qingyin.talk.data.json.UserBean
import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.usersession.UserSessionManager
import live.qingyin.talk.usersession.model.UserSession

class ProfileSettingViewModel(
    private val userSessionManager: UserSessionManager,
    private val userRepository: UserRepository
) : RxViewModel() {

    val userSessionLive = userSessionManager.liveDataOfUser()

    val userSession: UserSession
        get() = userSessionLive.value ?: userSessionManager.loadUser()!!

    fun modifyGender(gender: Int) {
        val userBean = UserBean()
        userBean.gender = gender

    }

    private fun modifyProfile(userBean: UserBean) {
        userRepository
            .modifyProfile(userSession.sessionToken, userSession.id, userBean)
            .schedule4Io2Main()
            ._subscribe {
                _onSubscribe {
                    it.disposeWhenCleared()
                }
                _onNext {

                }
                _onError {

                }
            }
    }
}