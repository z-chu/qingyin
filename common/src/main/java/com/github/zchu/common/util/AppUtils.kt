package com.github.zchu.common.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build

private fun Context.getPackageInfo(packageName: String? = null): PackageInfo? {
    try {
        val packageManager = this.packageManager
        return packageManager
            .getPackageInfo(packageName ?: this.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return null
}

fun Context.getAppVersionName(): String? {
    return this.getPackageInfo()?.versionName
}

fun Context.getAppVersionCode(): Long? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this.getPackageInfo()?.longVersionCode
    } else {
        this.getPackageInfo()?.versionCode?.toLong()
    }
}

fun Context.getAppIconDrawable(): Drawable? {
    try {
        val packageManager = this.applicationContext.packageManager
        val applicationInfo = packageManager.getApplicationInfo(
            this.packageName, 0
        )
        return packageManager.getApplicationIcon(applicationInfo)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return null
}

fun Context.getAppName(): String? {
    val labelRes = this.getPackageInfo()?.applicationInfo?.labelRes
    return labelRes?.let {
        this.getString(it)
    }
}

