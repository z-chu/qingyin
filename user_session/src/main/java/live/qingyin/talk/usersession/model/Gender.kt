package live.qingyin.talk.usersession.model

import live.qingyin.talk.usersession.R

inline class Gender(val gender: Int) {
    val stringRes: Int
        get() = when (gender) {
            1 -> R.string.gender_male
            2 -> R.string.gender_female
            else -> R.string.gender_unknown
        }
}