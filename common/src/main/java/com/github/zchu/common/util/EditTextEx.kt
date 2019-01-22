package com.github.zchu.common.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class TextWatcherObj : TextWatcher {

    private var _afterTextChanged: ((s: Editable?) -> Unit)? = null

    fun _afterTextChanged(func: ((s: Editable?) -> Unit)) {
        _afterTextChanged = func
    }

    override fun afterTextChanged(s: Editable?) {
        _afterTextChanged?.invoke(s)
    }

    private var _beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null

    fun _beforeTextChanged(func: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)) {
        _beforeTextChanged = func
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }


    private var _onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    fun _onTextChanged(func: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)) {
        _onTextChanged = func
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }
}

inline fun EditText._addTextChangedListener(func: (TextWatcherObj.() -> Unit)) =
    addTextChangedListener(TextWatcherObj().apply(func))

//may only available on real device
fun EditText.openKeyBoard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

//may only available on real device
fun EditText.hideKeyBoard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}