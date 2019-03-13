package live.qingyin.talk.usersession

import android.content.Context
import androidx.lifecycle.LiveData
import com.github.zchu.common.livedata.map
import com.github.zchu.common.util.checkNonNull
import live.qingyin.talk.usersession.model.User


class UserManager(context: Context) {


    private val userPreferences = UserPreferences(context.applicationContext)


    fun isLoggedIn(): Boolean {
        return checkNonNull(
            userPreferences.userId,
            userPreferences.username,
            userPreferences.sessionToken
        )
    }

    fun liveDataOfUser(): LiveData<User> {
        return userPreferences
            .userLiveData
            .map { it?.copy() }
    }

    fun loadUser(): User? {
        return userPreferences.loadUser()
    }

    fun saveUser(user: User) {
        userPreferences.saveUser(user)
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



