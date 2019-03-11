package live.qingyin.talk.data.bean

import com.google.gson.annotations.SerializedName

data class UserBean(
    @SerializedName("objectId")
    val id: String,
    val sessionToken: String
)