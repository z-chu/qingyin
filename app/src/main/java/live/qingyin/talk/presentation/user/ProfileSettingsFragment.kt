package live.qingyin.talk.presentation.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.rx.bindLifecycle
import com.github.zchu.common.util.DebounceOnClickLister
import com.github.zchu.model.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseFragment
import live.qingyin.talk.pref.profilePhotoUrls
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.*
import org.koin.android.viewmodel.ext.viewModel
import java.io.File

class ProfileSettingsFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ProfileSettingViewModel by viewModel()

    private val rxPermissions: RxPermissions by lazy(LazyThreadSafetyMode.NONE) {
        RxPermissions(this)
    }

    private val activityResultDispatcher: ActivityResultDispatcher by lazy(LazyThreadSafetyMode.NONE) {
        ActivityResultDispatcher()
    }

    override val layoutId: Int?
        get() = R.layout.fragment_profile_settings


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val debounceOnClickLister = DebounceOnClickLister(this)
        row_profile_photo.setOnClickListener(debounceOnClickLister)
        row_nickname.setOnClickListener(debounceOnClickLister)
        row_username.setOnClickListener(debounceOnClickLister)
        row_gender.setOnClickListener(debounceOnClickLister)
        row_bio.setOnClickListener(debounceOnClickLister)
        row_region.setOnClickListener(debounceOnClickLister)
        row_birthday.setOnClickListener(debounceOnClickLister)

        viewModel
            .userSessionLive
            .observe(this, Observer {
                displayUser(it)
            })

        viewModel
            .modifyStateLive
            .observe(this, Observer {
                when (it.status) {
                    Status.RUNNING -> {
                    }
                    Status.SUCCEEDED -> {
                        requireContext().showToastShort("修改成功")
                    }
                    Status.FAILED -> {
                        requireContext().showToastShort(it.throwable!!.getEasyMessage(requireContext()))
                    }
                }
            })
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("修改头像")
            .setItems(arrayOf("拍照", "从相册中选", "随机")) { dialog, which ->
                when (which) {
                    0 -> {
                        takePhoto4Camera()
                    }
                    1 -> {
                        takePhoto4Gallery()
                    }
                    2 -> {
                        viewModel.modifyProfilePhoto(profilePhotoUrls().random())
                    }
                }
            }
            .show()

    }

    private fun modifyNickname() {
        NameModifyActivity.start(requireContext())
    }

    private fun modifyGender() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("修改性别")
            .setItems(R.array.genders) { dialog, which ->
                if (which == viewModel.userSessionLive.value?.gender?.gender) {
                    dialog.dismiss()
                } else {
                    viewModel.modifyGender(which)
                }
            }
            .show()
    }

    private fun modifyBio() {
        val newIntent = BioModifyActivity.newIntent(requireContext(), viewModel.userSessionLive.value?.bio)
        activityResultDispatcher.startIntent(this, newIntent) { resultCode, data ->
            BioModifyActivity.getProfileBio(data)?.let {
                viewModel.modifyBio(it)
            }
        }
    }

    private fun modifyRegion() {

    }

    private fun modifyBirthday() {

    }

    private fun takePhoto4Camera() {
        rxPermissions
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe {
                if (it) {
                    openPhoto4Camera()
                }
            }
            .bindLifecycle(this)
    }

    private fun openPhoto4Camera() {
        val photoFile = generatePhotoFile() ?: return
        val intent = createTakePhotoIntent(requireContext(), photoFile)
        activityResultDispatcher.startIntent(this, intent) { resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                cropPhoto(photoFile)
            }
        }
    }

    private fun takePhoto4Gallery() {
        rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    openPhoto4Gallery()
                }
            }
            .bindLifecycle(this)
    }

    private fun openPhoto4Gallery() {
        val intent = createOpenAlbumIntent()
        activityResultDispatcher.startIntent(this, intent) { resultCode, data ->
            data?.data?.let {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = requireContext().contentResolver.query(it, filePathColumn, null, null, null)
                cursor?.run {
                    moveToFirst()
                    val columnIndex = getColumnIndex(filePathColumn[0])
                    val picturePath = getString(columnIndex)
                    close()
                    cropPhoto(File(picturePath))
                }
            }
        }
    }

    private fun generatePhotoFile(): File? {
        val externalCacheDir = requireContext().externalCacheDir ?: return null
        val fileDir = File("${externalCacheDir.path}/photo")
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            return null
        }
        val imagePath = externalCacheDir.path + "/photo" + File.separator + System.currentTimeMillis() + "Photo.jpg"
        return File(imagePath)
    }

    private fun cropPhoto(file: File) {
        val cropPhotoFile = generatePhotoFile() ?: return
        val cropPhotoIntent = createCropPhotoIntent(requireContext(), file, cropPhotoFile)
        activityResultDispatcher
            .startIntent(this, cropPhotoIntent) { resultCode: Int, data: Intent? ->
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.modifyProfilePhoto(cropPhotoFile)
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultDispatcher.dispatch(requestCode, resultCode, data)
    }
}