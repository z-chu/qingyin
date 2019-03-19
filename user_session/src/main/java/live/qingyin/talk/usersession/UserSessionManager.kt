package live.qingyin.talk.usersession

import android.content.Context
import androidx.lifecycle.LiveData
import com.github.zchu.common.livedata.map
import live.qingyin.talk.usersession.model.UserSession


class UserSessionManager(context: Context) {


    private val userPreferences = UserPreferences(context.applicationContext)


    fun isLoggedIn(): Boolean {
        return userPreferences.hasUser()
    }

    fun liveDataOfUser(): LiveData<UserSession> {
        return userPreferences
            .userLiveData
            .map { it?.copy() }
    }

    fun loadUser(): UserSession? {
        return userPreferences.loadUser()
    }

    fun saveUser(user: UserSession) {
        userPreferences.saveUser(user)
    }

    fun logout() {
        userPreferences.clear()
    }


    inline fun doIfLoggedIn(block: (UserSession) -> Unit): UserSessionManager {
        loadUser()?.let(block)
        return this
    }

    inline fun doIfnNotLoggedIn(block: () -> Unit): UserSessionManager {
        if (!isLoggedIn()) {
            block.invoke()
        }
        return this
    }
}



