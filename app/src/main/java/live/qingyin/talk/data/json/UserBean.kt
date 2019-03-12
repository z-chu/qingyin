package live.qingyin.talk.data.json

data class UserBean(val objectId: String, val username: String, val sessionToken: String) {
    var phone: String? = null

}

fun UserBean.toUser(): live.qingyin.talk.usersession.model.User {
    return live.qingyin.talk.usersession.model.User(objectId, username, sessionToken, phone)
}