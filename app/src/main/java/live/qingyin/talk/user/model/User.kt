package live.qingyin.talk.user.model

data class User(
    val id: String,
    val username: String,
    val sessionToken: String,
    var phone: String? = null
)
