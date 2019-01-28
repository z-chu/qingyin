package com.github.zchu.common.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.annotation.Px


@Px
fun Context.getNavigationBarHeight(): Int {
    val resourceId = this.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) this.resources.getDimensionPixelSize(resourceId) else 0
}

@Px
fun Context.getActionBarHeight(): Int {
    val tv = TypedValue()
    return if (this.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(
            tv.data, this.resources.displayMetrics
        )
    } else 0
}

@Px
fun Context.getStateBarHeight(): Int {
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) this.resources.getDimensionPixelSize(resourceId) else 0
}

fun Context.hasVirtualNavigationBar(): Boolean {
    var hasSoftwareKeys = true

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val d = (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)

        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        hasSoftwareKeys = realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        hasSoftwareKeys = !hasMenuKey && !hasBackKey
    }
    return hasSoftwareKeys
}


fun Context.getScreenResolution(): Point {
    val screenResolution = Point()
    val wm = this.getSystemService(Context.WINDOW_SERVICE)
    if (wm != null && wm is WindowManager) {
        val display = wm.defaultDisplay
        display.getSize(screenResolution)
    }
    return screenResolution
}

fun Context.getScreenOrientation(): Int {
    var orientation = Configuration.ORIENTATION_UNDEFINED
    val wm = this.getSystemService(Context.WINDOW_SERVICE)
    if (wm != null && wm is WindowManager) {
        val display = wm.defaultDisplay
        val screenResolution = Point()
        display.getSize(screenResolution)
        orientation = if (screenResolution.x == screenResolution.y) {
            Configuration.ORIENTATION_SQUARE
        } else {
            if (screenResolution.x < screenResolution.y) {
                Configuration.ORIENTATION_PORTRAIT
            } else {
                Configuration.ORIENTATION_LANDSCAPE
            }
        }
    }
    return orientation
}
