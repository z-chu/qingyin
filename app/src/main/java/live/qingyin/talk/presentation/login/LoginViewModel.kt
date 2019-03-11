package live.qingyin.talk.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zchu.model.ViewData
import live.qingyin.talk.user.model.User

class LoginViewModel : ViewModel() {
    //   private val userRepository by lazy { UserRepository() }

    private val viewData: MutableLiveData<ViewData<User>> by lazy {
        MutableLiveData<ViewData<User>>()
    }

    fun result(): LiveData<ViewData<User>> {
        return viewData
    }

    fun login(username: String, password: String) {
        /*    userRepository
                .loginOrRegister(username, password)
                .schedule4Io2Main()
                .subscribe(viewData)*/

    }

}

