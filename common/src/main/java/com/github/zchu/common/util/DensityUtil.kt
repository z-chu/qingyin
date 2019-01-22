package com.github.zchu.common.util

import android.content.Context
import android.util.TypedValue


fun Context.dp2px(dpVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpVal, this.resources.displayMetrics
    ).toInt()
}

fun Context.sp2px(spVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal, this.resources.displayMetrics
    ).toInt()
}

fun Context.px2dp(pxVal: Float): Float {
    val scale = this.resources.displayMetrics.density
    return pxVal / scale
}

fun Context.px2sp(pxVal: Float): Float {
    return pxVal / this.resources.displayMetrics.scaledDensity
}

