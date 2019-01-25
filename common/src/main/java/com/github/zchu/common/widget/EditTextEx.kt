package com.github.zchu.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

class TextWatcherBridge : TextWatcher {

    private var _afterTextChanged: ((text: Editable?) -> Unit)? = null
    private var _beforeTextChanged: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    private var _onTextChanged: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    fun _afterTextChanged(func: ((editable: Editable?) -> Unit)) {
        _afterTextChanged = func
    }

    fun _beforeTextChanged(func: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)) {
        _beforeTextChanged = func
    }

    fun _onTextChanged(func: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)) {
        _onTextChanged = func
    }

    override fun afterTextChanged(editable: Editable?) {
        _afterTextChanged?.invoke(editable)
    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(text, start, count, after)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(text, start, before, count)
    }
}

inline fun TextView._addTextChangedListener(func: (TextWatcherBridge.() -> Unit)) =
    addTextChangedListener(TextWatcherBridge().apply(func))

fun EditText.openKeyBoard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun EditText.hideKeyBoard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}