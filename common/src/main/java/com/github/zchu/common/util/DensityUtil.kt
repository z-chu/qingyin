package com.github.zchu.common.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.Dimension
import androidx.annotation.Px


@Px
fun Context.dp2px(@Dimension(unit = Dimension.DP) dpVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpVal, this.resources.displayMetrics
    ).toInt()
}

@Px
fun Context.sp2px(@Dimension(unit = Dimension.DP) spVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal, this.resources.displayMetrics
    ).toInt()
}

@Dimension(unit = Dimension.DP)
fun Context.px2dp(@Px pxVal: Int): Float {
    val scale = this.resources.displayMetrics.density
    return pxVal / scale
}

@Dimension(unit = Dimension.SP)
fun Context.px2sp(@Px pxVal: Int): Float {
    return pxVal / this.resources.displayMetrics.scaledDensity
}

