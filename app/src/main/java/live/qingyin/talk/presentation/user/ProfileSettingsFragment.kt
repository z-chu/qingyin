package live.qingyin.talk.presentation.user

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.github.zchu.common.util.DebounceOnClickLister
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseFragment
import live.qingyin.talk.usersession.UserSessionManager
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.GlideApp
import org.koin.android.ext.android.inject

class ProfileSettingsFragment : BaseFragment(), View.OnClickListener {


    private val userSessionManager: UserSessionManager by inject()

    override val layoutId: Int?
        get() = R.layout.fragment_profile_settings


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSessionManager
            .liveDataOfUser()
            .observe(this, Observer {
                displayUser(it)
            })
        row_profile_photo.setOnClickListener(DebounceOnClickLister(this))
        row_nickname.setOnClickListener(DebounceOnClickLister(this))
        row_username.setOnClickListener(DebounceOnClickLister(this))
        row_gender.setOnClickListener(DebounceOnClickLister(this))
        row_bio.setOnClickListener(DebounceOnClickLister(this))
        row_region.setOnClickListener(DebounceOnClickLister(this))
        row_birthday.setOnClickListener(DebounceOnClickLister(this))
    }

    private fun displayUser(userSession: UserSession) {
        userSession.run {
            GlideApp.with(this@ProfileSettingsFragment)
                .load(userSession.profilePhoto)
                .into(iv_profile_photo)
            row_nickname.setSubtitle(name)
            row_gender.setSubtitle(gender.toString())
            row_bio.setSubtitle(bio ?: getString(R.string.default_bio_if_null))
            row_region.setSubtitle(region)
            row_birthday.setSubtitle(birthday?.toString())
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.row_profile_photo -> {

            }
            R.id.row_nickname -> {

            }

            R.id.row_gender -> {

            }
            R.id.row_bio -> {

            }
            R.id.row_region -> {

            }
            R.id.row_birthday -> {

            }
        }
    }

    fun modifyProfilePhoto() {

    }

    fun modifyNickname() {

    }

    fun modifyGender() {

    }

    fun modifyBio() {

    }

    fun modifyRegion() {

    }

    fun modifyBirthday() {

    }
}