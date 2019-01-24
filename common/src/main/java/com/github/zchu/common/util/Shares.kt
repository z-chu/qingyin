package com.github.zchu.common.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.StringRes
import java.io.File
import java.util.*

fun Context.shareText(@StringRes resId: Int) {
    shareText(getString(resId))
}

fun Context.shareText(shareText: String, shareSubject: String = "share") {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
    intent.putExtra(Intent.EXTRA_TEXT, shareText)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(
        Intent.createChooser(intent, shareSubject)
    )
}


fun Context.shareImage(imageFile: File, shareSubject: String = "share") {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/*"
    val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        getImageContentUri(imageFile)
    } else {
        Uri.fromFile(imageFile)
    }
    if (uri != null) {
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(
            Intent.createChooser(intent, shareSubject)
        )
    }
}

fun Context.shareImage(files: ArrayList<File>) {
    val imageUris = ArrayList<Uri>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        for (f in files) {
            val imageContentUri = getImageContentUri(f)
            if (imageContentUri != null) {
                imageUris.add(imageContentUri)
            }
        }
    } else {
        for (f in files) {
            imageUris.add(Uri.fromFile(f))
        }
    }
    if (imageUris.isNotEmpty()) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE//设置分享行为
        shareIntent.type = "image/*"//设置分享内容的类型
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        startActivity(Intent.createChooser(shareIntent, "Share"))
    }

}

fun Context.getImageContentUri(imageFile: File): Uri? {
    val filePath = imageFile.absolutePath
    val cursor = contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ",
        arrayOf(filePath), null
    )
    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        cursor.close()
        val baseUri = Uri.parse("content://media/external/images/media")
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (imageFile.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            null
        }
    }
}