package live.qingyin.talk.user

import android.content.Context
import androidx.lifecycle.LiveData
import com.github.zchu.common.livedata.map
import com.github.zchu.common.util.checkNonNull
import live.qingyin.talk.user.model.User
import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener
import net.grandcentrix.tray.core.TrayItem


class UserManager(context: Context) {


    private val userPreferences = UserPreferences(context.applicationContext)

    private val userLive: LiveData<User> by lazy { UserLiveData(userPreferences) }


    fun isLoggedIn(): Boolean {
        return checkNonNull(
            userPreferences.userId,
            userPreferences.username,
            userPreferences.sessionToken
        )
    }

    fun liveDataOfUser(): LiveData<User> {
        return userLive.map { it?.copy() }
    }

    fun loadUser(): User? {
        return userPreferences.loadUser()
    }

    fun saveUser(user: User) {
        userPreferences.userId = user.id
        userPreferences.username = user.username
        userPreferences.sessionToken = user.sessionToken
        userPreferences.phone = user.phone
        userPreferences.userVersion++
    }

    fun logout() {
        userPreferences.clear()
    }


    inline fun doIfLoggedIn(block: (User) -> Unit): UserManager {
        loadUser()?.let(block)
        return this
    }

    inline fun doIfnNotLoggedIn(block: () -> Unit): UserManager {
        if (!isLoggedIn()) {
            block.invoke()
        }
        return this
    }
}


private class UserLiveData(val userPreferences: UserPreferences) : LiveData<User>(), OnTrayPreferenceChangeListener {

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
