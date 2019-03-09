package live.qingyin.talk.user

import android.content.Context
import androidx.lifecycle.LiveData
import com.github.zchu.common.livedata.map
import live.qingyin.talk.user.model.User
import net.grandcentrix.tray.TrayPreferences
import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener
import net.grandcentrix.tray.core.TrayItem


class UserManager(context: Context) {


    private val userPreferences = UserPreferences(context.applicationContext)

    private val userLive: LiveData<User> by lazy { UserLiveData(userPreferences) }


    fun isLoggedIn(): Boolean {
        return getUser() != null
    }

    fun liveDataOfUser(): LiveData<User> {
        return userLive.map { it?.copy() }
    }

    fun getUser(): User? {
        return userLive.value?.copy()
    }


    fun saveUser(user: User) {
    }


    inline fun doIfLoggedIn(block: (User) -> Unit): UserManager {
        if (isLoggedIn()) {
            block.invoke(getUser()!!)
        }
        return this
    }

    inline fun doIfnNotLoggedIn(block: () -> Unit): UserManager {
        if (!isLoggedIn()) {
            block.invoke()
        }
        return this
    }


}

private class UserPreferences(context: Context) : TrayPreferences(context, "user_session", 1)


private class UserLiveData(val userPreferences: UserPreferences) : LiveData<User>(), OnTrayPreferenceChangeListener {

    init {
        userPreferences.registerOnTrayPreferenceChangeListener(this)
    }

    private fun loadUser(): User? {
        TODO()

    }

    override fun onTrayPreferenceChanged(items: MutableCollection<TrayItem>) {

    }

    override fun onActive() {
        super.onActive()
        userPreferences.registerOnTrayPreferenceChangeListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        userPreferences.unregisterOnTrayPreferenceChangeListener(this)
    }


}
