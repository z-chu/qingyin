package live.qingyin.talk.data.json

import live.qingyin.talk.usersession.model.Gender
import live.qingyin.talk.usersession.model.UserSession

class UserBean {
    var objectId: String? = null
    var username: String? = null
    var sessionToken: String? = null
    var phone: String? = null //手机
    var gender: Int? = null //性别  0保密、1男、2女
    var name: String? = null //名称
    var profilePhoto: String? = null //头像
    var cover: String? = null //封面
    var bio: String? = null //个人介绍
    var region: String? = null //地区
    var birthday: LCDate? = null // 生日

    override fun toString(): String {
        return "UserBean(objectId=$objectId, username=$username, sessionToken=$sessionToken, phone=$phone," +
                " gender=$gender, name=$name, profilePhoto=$profilePhoto, cover=$cover, bio=$bio, region=$region, birthday=$birthday)"
    }
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
        birthday?.date
    )
}

fun UserBean.applyTo(userSession: UserSession): UserSession {
    return UserSession(
        objectId ?: userSession.id,
        username ?: userSession.username,
        sessionToken ?: userSession.sessionToken,
        name ?: userSession.name ?: username!!,
        Gender(gender ?: userSession.gender.gender),
        phone ?: userSession.phone,
        profilePhoto ?: userSession.profilePhoto,
        cover ?: userSession.cover,
        bio ?: userSession.bio,
        region ?: userSession.region,
        birthday?.date ?: userSession.birthday
    )
}

