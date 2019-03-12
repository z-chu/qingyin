package live.qingyin.talk.data.net

import io.reactivex.Observable
import live.qingyin.talk.data.json.LCResult
import live.qingyin.talk.data.json.UserBean
import live.qingyin.talk.data.json.UserBody
import retrofit2.http.*

const val H_LC_ID = "X-LC-Id"
const val H_LC_KEY = "X-LC-Key"
const val H_LC_SESSION = "X-LC-Session"

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
    fun putUser(@Header(H_LC_SESSION) sessionToken: String, @Path("objectId") userId: String, @Body user: UserBean): Observable<LCResult<*>>

    @GET("users/me")
    fun me(@Header(H_LC_SESSION) sessionToken: String)


}