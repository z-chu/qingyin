package com.github.zchu.common.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

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


fun Context.getApkPackageName(path: String): String? {
    val pm = this.packageManager
    val info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
    if (info != null) {
        val appInfo = info.applicationInfo
        return appInfo.packageName
    }
    return null
}


fun Context.isAppInstall(packageName: String): Boolean {
    try {
        if (this.packageManager.getPackageInfo(
                packageName, 0
            ) != null
        ) {
            return true
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return false
}

fun Context.startApplicationWithPackageName(packageName: String) {
    // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
    val packageInfo: PackageInfo = this.getPackageInfo(packageName) ?: return

    // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
    val resolveIntent = Intent(Intent.ACTION_MAIN, null)
    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    resolveIntent.setPackage(packageInfo.packageName)

    // 通过getPackageManager()的queryIntentActivities方法遍历
    val resolveInfoList = this.packageManager
        .queryIntentActivities(resolveIntent, 0)

    val resolveInfo = resolveInfoList.iterator().next()
    if (resolveInfo != null) {
        // packagename = 参数packname
        val resolvePackageName = resolveInfo.activityInfo.packageName
        // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
        val className = resolveInfo.activityInfo.name
        // LAUNCHER Intent
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        // 设置ComponentName参数1:packagename参数2:MainActivity路径
        val cn = ComponentName(resolvePackageName, className)

        intent.component = cn
        this.startActivity(intent)
    }
}

fun Context.installApk(apkFilePath: String, fileProviderAuthority: String) {
    this.installApk(File(apkFilePath), fileProviderAuthority)
}

fun Context.installApk(apkFile: File, fileProviderAuthority: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val fileUri = FileProvider.getUriForFile(this, fileProviderAuthority, apkFile)
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    } else {
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")

    }
    this.startActivity(intent)
}


fun Context.showAppSystemSetting() {
    val localIntent = Intent()
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    localIntent.data = Uri.fromParts("package", packageName, null)
    startActivity(localIntent)
}

fun Context.showAppMarket(): Boolean {
    return try {
        val mAddress = "market://details?id=$packageName"
        val marketIntent = Intent("android.intent.action.VIEW")
        marketIntent.data = Uri.parse(mAddress)
        startActivity(marketIntent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

}