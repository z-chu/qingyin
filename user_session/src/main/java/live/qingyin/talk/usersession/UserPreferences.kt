package live.qingyin.talk.usersession

import android.content.Context
import androidx.lifecycle.LiveData
import com.github.zchu.common.util.checkNonNull
import live.qingyin.talk.usersession.model.UserSession
import net.grandcentrix.tray.TrayPreferences
import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener
import net.grandcentrix.tray.core.TrayItem
import java.util.*


internal class UserPreferences(context: Context) {

    private val preferences = TrayPreferences(context, "user_session", 1)

    private var userVersion: Int
        set(value) {
            preferences.put(K_USER_VERSION, value)
        }
        get() = preferences.getInt(K_USER_VERSION, 0)

    private var sessionToken: String?
        set(value) {
            preferences.put(K_SESSION_TOKEN, value)
        }
        get() = preferences.getString(K_SESSION_TOKEN, null)

    private var userId: String?
        set(value) {
            preferences.put(K_USER_ID, value)
        }
        get() = preferences.getString(K_USER_ID, null)


    private var username: String?
        set(value) {
            preferences.put(K_USER_NAME, value)
        }
        get() = preferences.getString(K_USER_NAME, null)

    private var phone: String?
        set(value) {
            preferences.put(K_PHONE, value)
        }
        get() = preferences.getString(K_PHONE, null)

    private var gender: Int
        set(value) {
            preferences.put(K_GENDER, value)
        }
        get() = preferences.getInt(K_GENDER, 0)

    private var name: String?
        set(value) {
            preferences.put(K_NAME, value)
        }
        get() = preferences.getString(K_NAME, null)

    private var profilePhoto: String?
        set(value) {
            preferences.put(K_PROFILE_PHOTO, value)
        }
        get() = preferences.getString(K_PROFILE_PHOTO, null)

    private var cover: String?
        set(value) {
            preferences.put(K_COVER, value)
        }
        get() = preferences.getString(K_COVER, null)

    private var bio: String?
        set(value) {
            preferences.put(K_BIO, value)
        }
        get() = preferences.getString(K_BIO, null)

    private var region: String?
        set(value) {
            preferences.put(K_REGION, value)
        }
        get() = preferences.getString(K_REGION, null)

    private var birthday: Date?
        set(value) {
            preferences.put(K_BIRTHDAY, value?.time ?: 0)
        }
        get() {
            val time = preferences.getLong(K_BIRTHDAY, 0)
            if (time > 0) {
                return Date(time)
            }
            return null
        }

    fun hasUser(): Boolean {
        return checkNonNull(
            userId,
            username,
            sessionToken
        )
    }

    fun loadUser(): UserSession? {
        val userId = userId ?: return null
        val username = username ?: return null
        val sessionToken = sessionToken ?: return null
        val name = name ?: username
        return UserSession(
            userId,
            username,
            sessionToken,
            phone = phone,
            gender = gender,
            name = name,
            profilePhoto = profilePhoto,
            cover = cover,
            bio = bio,
            region = region,
            birthday = birthday
        )
    }

    fun saveUser(user: UserSession) {
        userId = user.id
        username = user.username
        sessionToken = user.sessionToken
        phone = user.phone
        gender = user.gender
        name = user.name
        profilePhoto = user.profilePhoto
        cover = user.cover
        bio = user.bio
        region = user.region
        birthday = user.birthday
        userVersion++
    }


    fun clear() {
        preferences.clear()
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

            userPreferences.preferences.registerOnTrayPreferenceChangeListener(this)
        }

        override fun onInactive() {
            super.onInactive()
            userPreferences.preferences.unregisterOnTrayPreferenceChangeListener(this)
        }

    }

    companion object {
        private const val K_USER_VERSION = "user_version"
        private const val K_SESSION_TOKEN = "session_token"
        private const val K_USER_ID = "user_id"
        private const val K_USER_NAME = "user_name"
        private const val K_PHONE = "phone"
        private const val K_GENDER = "gender"
        private const val K_NAME = "name"
        private const val K_PROFILE_PHOTO = "profilePhoto"
        private const val K_COVER = "cover"
        private const val K_BIO = "bio"
        private const val K_REGION = "region"
        private const val K_BIRTHDAY = "birthday"
    }
}
