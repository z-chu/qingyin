package live.qingyin.talk.data.json

import live.qingyin.talk.user.model.User

data class UserBean(val objectId: String, val username: String, val sessionToken: String) {
    var phone: String? = null

}

fun UserBean.toUser(): User {
    return User(objectId, username, sessionToken, phone)
}