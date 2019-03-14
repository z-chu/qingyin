package live.qingyin.talk.data.json

data class UserBean(val objectId: String, val username: String, val sessionToken: String) {
    var phone: String? = null

}

fun UserBean.toUser(): live.qingyin.talk.usersession.model.UserSession {
    return live.qingyin.talk.usersession.model.UserSession(objectId, username, sessionToken, phone)
}