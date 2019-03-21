package live.qingyin.talk.presentation.user

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.github.zchu.common.util.DebounceOnClickLister
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseFragment
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.GlideApp
import org.koin.android.viewmodel.ext.viewModel

class ProfileSettingsFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ProfileSettingViewModel by viewModel()

    override val layoutId: Int?
        get() = R.layout.fragment_profile_settings


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
            .userSessionLive
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
            row_gender.setSubtitle(getString(gender.stringRes))
            row_bio.setSubtitle(bio ?: getString(R.string.default_bio_if_null))
            row_region.setSubtitle(region)
            row_birthday.setSubtitle(birthday?.toString())
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.row_profile_photo -> {
                modifyProfilePhoto()
            }
            R.id.row_nickname -> {
                modifyNickname()
            }
            R.id.row_gender -> {
                modifyGender()
            }
            R.id.row_bio -> {
                modifyBio()
            }
            R.id.row_region -> {
                modifyRegion()
            }
            R.id.row_birthday -> {
                modifyBirthday()
            }
        }
    }

    private fun modifyProfilePhoto() {

    }

    private fun modifyNickname() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("修改昵称")
            .setMessage("好的昵称让人更容易记住你")
            .setItems(arrayOf("保密", "男", "女")) { dialog, which ->

            }


    }

    private fun modifyGender() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("修改性别")
            .setItems(arrayOf("保密", "男", "女")) { dialog, which ->
                if (which == viewModel.userSessionLive.value?.gender?.gender) {
                    dialog.dismiss()
                } else {
                    viewModel.modifyGender(which)
                }
            }
            .show()
    }

    private fun modifyBio() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("修改性别")
            .setItems(arrayOf("保密", "男", "女")) { dialog, which ->

            }
            .show()
    }

    private fun modifyRegion() {

    }

    private fun modifyBirthday() {

    }
}