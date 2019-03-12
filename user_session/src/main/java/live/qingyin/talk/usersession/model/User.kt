package live.qingyin.talk.usersession.model

data class User(
    val id: String,
    val username: String,
    val sessionToken: String,
    var phone: String? = null
)
