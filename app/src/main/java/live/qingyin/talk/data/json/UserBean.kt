package live.qingyin.talk.data.json

import live.qingyin.talk.usersession.model.UserSession

data class UserBean(val objectId: String, val username: String, val sessionToken: String) {
    var phone: String? = null

}

fun UserBean.toUser(): UserSession {
    return UserSession(objectId, username, sessionToken, phone)
}