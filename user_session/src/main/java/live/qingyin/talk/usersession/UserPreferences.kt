package live.qingyin.talk.usersession

import android.content.Context
import live.qingyin.talk.usersession.model.User
import net.grandcentrix.tray.TrayPreferences


internal class UserPreferences(context: Context) : TrayPreferences(context, "user_session", 1) {

    var userVersion: Int
        set(value) {
            put(K_USER_VERSION, value)
        }
        get() = getInt(K_USER_VERSION, 0)

    var sessionToken: String?
        set(value) {
            put(K_SESSION_TOKEN, value)
        }
        get() = getString(K_SESSION_TOKEN, null)

    var userId: String?
        set(value) {
            put(K_USER_ID, value)
        }
        get() = getString(K_USER_ID, null)


    var username: String?
        set(value) {
            put(K_USER_NAME, value)
        }
        get() = getString(K_USER_NAME, null)

    var phone: String?
        set(value) {
            put(K_PHONE, value)
        }
        get() = getString(K_PHONE, null)


    fun loadUser(): User? {
        val userId = userId ?: return null
        val username = username ?: return null
        val sessionToken = sessionToken ?: return null
        return User(userId, username, sessionToken, phone)
    }


    companion object {
        const val K_USER_VERSION = "user_version"
        private const val K_SESSION_TOKEN = "session_token"
        private const val K_USER_ID = "user_id"
        private const val K_USER_NAME = "user_name"
        private const val K_PHONE = "phone"
    }
}
