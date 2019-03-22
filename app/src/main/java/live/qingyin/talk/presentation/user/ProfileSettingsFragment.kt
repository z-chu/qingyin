package live.qingyin.talk.presentation.user

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.zchu.common.help.showToastShort
import com.github.zchu.common.rx.bindLifecycle
import com.github.zchu.common.util.DebounceOnClickLister
import com.github.zchu.model.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import live.qingyin.talk.BuildConfig
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseFragment
import live.qingyin.talk.pref.profilePhotoUrls
import live.qingyin.talk.usersession.model.UserSession
import live.qingyin.talk.utils.GlideApp
import live.qingyin.talk.utils.getEasyMessage
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("修改昵称")
            .setMessage("好的昵称让人更容易记住你")
            .setItems(arrayOf("保密", "男", "女")) { dialog, which ->

            }


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
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider
                    .getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".photo.provider", photoFile)
            )
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
        }
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
        startSystemAvatarCrop(this, file, cropPhotoFile, 0X33)
        activityResultDispatcher.subscribe(-11) { _, data ->
            GlideApp.with(this@ProfileSettingsFragment)
                .load(cropPhotoFile)
                .placeholder(R.mipmap.ic_launcher)
                .into(iv_profile_photo)
        }

    }

    /**
     * 调用系统头像裁剪
     */
    fun startSystemAvatarCrop(context: Fragment, inputFile: File, outputFile: File, requestCode: Int) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(
            getImageContentUri(context.requireContext(), inputFile),
            "image/*"
        )//自己使用Content Uri替换File Uri
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)
        context.startActivityForResult(intent, requestCode)

    }

    /**
     * 转换 content:// uri  ，调用系统头像裁剪用到
     */
    fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(
                cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID)
            )
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                return null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultDispatcher.dispatch(requestCode, resultCode, data)
    }
}