package live.qingyin.talk.data.net

import io.reactivex.Observable
import live.qingyin.talk.data.json.LCResult
import live.qingyin.talk.data.json.UserBean
import live.qingyin.talk.data.json.UserBody
import retrofit2.http.*

interface LeancloudService {
    /**
     * 注册用户
     * username，password是必须字段
     */
    @POST("users")
    fun register(@Body body: UserBody): Observable<UserBean>

    @POST("login")
    fun login(@Query("username") username: String, @Query("password") password: String): Observable<UserBean>

    @PUT("users/{objectId}")
    fun updateUser(@Path("objectId") userId: String, @Header("X-LC-Session") sessionToken: String, @Body user: UserBean): Observable<LCResult<*>>
}