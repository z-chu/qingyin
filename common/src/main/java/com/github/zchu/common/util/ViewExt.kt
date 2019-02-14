package com.github.zchu.common.util

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup


fun View.removeSelfFromParent() {
    val parent = this.parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

fun View.isTouchInView(ev: MotionEvent): Boolean {
    val vLoc = IntArray(2)
    getLocationOnScreen(vLoc)
    val motionX = ev.rawX
    val motionY = ev.rawY
    return motionX >= vLoc[0] && motionX <= vLoc[0] + width && motionY >= vLoc[1] && motionY <= vLoc[1] + height
}

/**
 * 获取view在屏幕中的绝对x坐标
 */
fun View.getAbsoluteX(): Float {
    var x = x
    val parent = parent
    if (parent is View) {
        x += parent.getAbsoluteX()
    }
    return x
}

/**
 * 获取view在屏幕中的绝对y坐标
 */
fun View.getAbsoluteY(): Float {
    var y = y
    val parent = parent
    if (parent is View) {
        y += parent.getAbsoluteY()
    }
    return y
}

fun <T : View> View.findViewByClass(clazz: Class<T>): T? {
    if (clazz.isAssignableFrom(this::class.java)) {
        return this as T
    }
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val findViewByClass = this.getChildAt(i).findViewByClass(clazz)
            if (findViewByClass != null) {
                return findViewByClass
            }
        }

    }
    return null
}