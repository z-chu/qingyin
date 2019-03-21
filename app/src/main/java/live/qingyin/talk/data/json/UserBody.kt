package live.qingyin.talk.data.json

class UserBody(val username: String? = null, val password: String? = null) {

    constructor() : this(null, null)

    var gender: Int? = null //性别  0保密、1男、2女
    var name: String? = null //名称
    var profilePhoto: String? = null //头像
}