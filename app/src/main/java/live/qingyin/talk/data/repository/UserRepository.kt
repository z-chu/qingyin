package live.qingyin.talk.data.repository

import com.google.gson.Gson
import io.reactivex.Observable
import live.qingyin.talk.data.bean.LCResult
import live.qingyin.talk.data.bean.UserBean
import live.qingyin.talk.data.body.UserBody
import live.qingyin.talk.data.net.LeancloudService
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
        return leancloudService
            .register(UserBody(username, password))
            .onErrorResumeNext { throwable: Throwable ->
                if (throwable is HttpException) {
                    val httpException = throwable
                    if (httpException.code() == 400) {
                        try {
                            val string = httpException.response().errorBody()!!.string()
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
}