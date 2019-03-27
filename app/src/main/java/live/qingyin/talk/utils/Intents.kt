package live.qingyin.talk.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import live.qingyin.talk.BuildConfig
import java.io.File

fun createCropPhotoIntent(context: Context, inputFile: File, outputFile: File): Intent {
    val intent = Intent("com.android.camera.action.CROP")
    intent.setDataAndType(
        getImageContentUri(context, inputFile),
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
    return intent
}

fun createTakePhotoIntent(context: Context, outputFile: File): Intent {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider
                .getUriForFile(context, BuildConfig.APPLICATION_ID + ".photo.provider", outputFile)
        )
    } else {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
    }
    return intent
}

fun createOpenAlbumIntent(): Intent {
    val intent: Intent
    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
    } else {
        intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }
    return intent
}


/**
 * 转换 content:// uri  ，调用系统头像裁剪用到
 */
private fun getImageContentUri(context: Context, imageFile: File): Uri? {
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
        cursor.close()
        val baseUri = Uri.parse("content://media/external/images/media")
        return Uri.withAppendedPath(baseUri, "" + id)
    } else {
        return if (imageFile.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
        } else {
            null
        }
    }
}