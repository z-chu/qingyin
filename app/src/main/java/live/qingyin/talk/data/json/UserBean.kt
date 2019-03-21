package live.qingyin.talk.data.json

import live.qingyin.talk.usersession.model.Gender
import live.qingyin.talk.usersession.model.UserSession
import java.util.*

class UserBean {
    var objectId: String? = null
    var username: String? = null
    var sessionToken: String? = null
    var phone: String? = null //手机
    var gender: Int? = 0 //性别  0保密、1男、2女
    var name: String? = null //名称
    var profilePhoto: String? = null //头像
    var cover: String? = null //封面
    var bio: String? = null //个人介绍
    var region: String? = null //地区
    var birthday: Date? = null // 生日
}

fun UserBean.toUserSession(): UserSession {
    return UserSession(
        objectId!!,
        username!!,
        sessionToken!!,
        name ?: username!!,
        Gender(gender ?: 0),
        phone,
        profilePhoto,
        cover,
        bio,
        region,
        birthday
    )
}

