package live.qingyin.talk.presentation.user

import androidx.lifecycle.LiveData
import com.github.zchu.bridge._subscribe
import com.github.zchu.common.livedata.SingleLiveEvent
import com.github.zchu.common.rx.RxViewModel
import com.github.zchu.common.rx.schedule4Io2Main
import com.github.zchu.model.WorkState
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import live.qingyin.talk.data.json.UserBean
import live.qingyin.talk.data.json.applyTo
import live.qingyin.talk.data.repository.FileUploader
import live.qingyin.talk.data.repository.UserRepository
import live.qingyin.talk.usersession.UserSessionManager
import live.qingyin.talk.usersession.model.UserSession
import java.io.File

class ProfileSettingViewModel(
    private val userSessionManager: UserSessionManager,
    private val userRepository: UserRepository,
    private val uploader: FileUploader
) : RxViewModel() {

    val userSessionLive = userSessionManager.liveDataOfUser()

    private val userSession: UserSession
        get() = userSessionLive.value ?: userSessionManager.loadUser()!!


    private val modifyState: SingleLiveEvent<WorkState> = SingleLiveEvent()

    val modifyStateLive: LiveData<WorkState>
        get() = modifyState

    fun modifyProfilePhoto(file: File) {
        uploader
            .uploadImage(file.name, file)
            .flatMap {
                val userBean = UserBean()
                userBean.profilePhoto = it
                userRepository
                    .modifyProfile(userSession.sessionToken, userSession.id, userBean)
            }
            .schedule4Io2Main()
            .subscribeModifyProfile()
    }


    fun modifyProfilePhoto(url: String) {
        val userBean = UserBean()
        userBean.profilePhoto = url
        modifyProfile(userBean)
    }

    fun modifyGender(gender: Int) {
        val userBean = UserBean()
        userBean.gender = gender
        modifyProfile(userBean)
    }

    private fun modifyProfile(userBean: UserBean) {
        userRepository
            .modifyProfile(userSession.sessionToken, userSession.id, userBean)
            .schedule4Io2Main()
            .subscribeModifyProfile()
    }


    private fun Observable<UserBean>.subscribeModifyProfile() {
        this._subscribe {
            _onSubscribe {
                it.disposeWhenCleared()
                modifyState.value = WorkState.LOADING
            }
            _onNext {
                userSessionManager.saveUser(it.applyTo(userSession))
                modifyState.value = WorkState.LOADED
            }
            _onError {
                Logger.e(it, "subscribeModifyProfile")
                modifyState.value = WorkState.error(it)
            }
        }
    }
}