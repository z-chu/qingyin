package live.qingyin.talk.usersession.model

import java.util.*

data class UserSession(
    val id: String,
    val username: String,
    val sessionToken: String,
    var name: String, //名称
    var gender: Int,//性别  0保密、1男、2女
    var phone: String? = null, //手机
    var profilePhoto: String? = null, //头像
    var cover: String? = null,//封面
    var bio: String? = null, //个人介绍
    var region: String? = null,//地区
    var birthday: Date? = null // 生日
)
