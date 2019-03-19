package live.qingyin.talk.data.json

import live.qingyin.talk.usersession.model.UserSession
import java.util.*

data class UserBean(val objectId: String, val username: String, val sessionToken: String) {
    var phone: String? = null //手机
    var gender: Int? = 0 //性别  0保密、1男、2女
    var name: String? = null //名称
    var profilePhoto: String? = null //头像
    var cover: String? = null //封面
    var bio: String? = null //个人介绍
    var region: String? = null //地区
    var birthday: Date? = null // 生日
}

fun UserBean.toUser(): UserSession {
    return UserSession(
        objectId,
        username,
        sessionToken,
        name ?: username,
        gender ?: 0,
        phone,
        profilePhoto,
        cover,
        bio,
        region,
        birthday
    )
}