package live.qingyin.talk.usersession

import android.content.Context
import androidx.lifecycle.LiveData
import live.qingyin.talk.usersession.model.UserSession
import net.grandcentrix.tray.TrayPreferences
import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener
import net.grandcentrix.tray.core.TrayItem


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


    fun loadUser(): UserSession? {
        val userId = userId ?: return null
        val username = username ?: return null
        val sessionToken = sessionToken ?: return null
        return UserSession(userId, username, sessionToken, phone)
    }

    fun saveUser(user: UserSession) {
        userId = user.id
        username = user.username
        sessionToken = user.sessionToken
        phone = user.phone
        userVersion++
    }


    val userLiveData: LiveData<UserSession> by lazy {
        UserLiveData(this)
    }

    private class UserLiveData(val userPreferences: UserPreferences) : LiveData<UserSession>(),
        OnTrayPreferenceChangeListener {

        private val userVersion = 0

        override fun onTrayPreferenceChanged(items: MutableCollection<TrayItem>) {
            for (item in items) {
                if (item.key() == UserPreferences.K_USER_VERSION) {
                    if (userVersion != item.value()?.toIntOrNull()) {
                        value = userPreferences.loadUser()
                    }
                }
            }
        }

        override fun onActive() {
            super.onActive()
            if (userVersion != userPreferences.userVersion) {
                value = userPreferences.loadUser()
            }
            userPreferences.registerOnTrayPreferenceChangeListener(this)
        }

        override fun onInactive() {
            super.onInactive()
            userPreferences.unregisterOnTrayPreferenceChangeListener(this)
        }

    }

    companion object {
        private const val K_USER_VERSION = "user_version"
        private const val K_SESSION_TOKEN = "session_token"
        private const val K_USER_ID = "user_id"
        private const val K_USER_NAME = "user_name"
        private const val K_PHONE = "phone"
    }
}
