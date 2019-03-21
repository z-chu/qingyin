package live.qingyin.talk.data.repository

import com.google.gson.Gson
import io.reactivex.Observable
import live.qingyin.talk.data.json.LCResult
import live.qingyin.talk.data.json.UserBean
import live.qingyin.talk.data.json.UserBody
import live.qingyin.talk.data.net.LeancloudService
import live.qingyin.talk.pref.profilePhotoUrls
import retrofit2.HttpException
import java.io.IOException

class UserRepository(private val leancloudService: LeancloudService) {

    fun login(username: String, password: String): Observable<UserBean> {
        return leancloudService
            .login(username, password)
    }

    fun register(username: String, password: String): Observable<UserBean> {
        return leancloudService
            .register(UserBody(username, password))
    }

    fun loginOrRegister(username: String, password: String): Observable<UserBean> {
        val userBody = UserBody(username, password)
        val profilePhotoUrls = profilePhotoUrls()
        userBody.profilePhoto = profilePhotoUrls.random()
        userBody.name = username
        return leancloudService
            .register(userBody)
            .onErrorResumeNext { throwable: Throwable ->
                if (throwable is HttpException) {
                    if (throwable.code() == 400) {
                        try {
                            val string = throwable.response().errorBody()!!.string()
                            val lcResult = Gson().fromJson(string, LCResult::class.java)
                            if (lcResult.code == 202) {
                                return@onErrorResumeNext leancloudService
                                    .login(username, password)
                            }
                        } catch (ignored: IOException) {

                        }

                    }

                }
                return@onErrorResumeNext Observable.error<UserBean>(throwable)

            }
    }

    fun modifyProfile(sessionToken: String, userId: String, userBean: UserBean): Observable<Unit> {
        return leancloudService
            .putUser(sessionToken, userId, userBean)
            .map { Unit }

    }
}