package com.github.zchu.common.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.openKeyBoard(): Boolean {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun EditText.hideKeyBoard(): Boolean {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Activity.showKeyBoard(): Boolean {
    val view = this.currentFocus
    if (view != null) {
        val imm = view.context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }
    return false
}

fun Activity.hideKeyBoard(): Boolean {
    val currentFocus = this.currentFocus
    if (currentFocus != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
    return false
}


fun Context.isKeyBoardActivated(): Boolean {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isActive
}