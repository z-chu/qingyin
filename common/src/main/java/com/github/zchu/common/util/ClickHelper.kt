package com.github.zchu.common.util

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

private fun View.doBindOnClickLister(listener: View.OnClickListener, ids: IntArray) {
    for (id in ids) {
        val view = findViewById<View>(id)
        view?.setOnClickListener(listener)
    }
}

fun View.bindOnClickLister(listener: View.OnClickListener, @IdRes vararg ids: Int) {
    doBindOnClickLister(listener, ids)
}

fun View.bindOnClickLister(@IdRes vararg ids: Int, block: ((v: View) -> Unit)) {
    doBindOnClickLister(block.toOnClickLister(), ids)
}

fun Activity.bindOnClickLister(listener: View.OnClickListener, @IdRes vararg ids: Int) {
    window?.decorView?.doBindOnClickLister(listener, ids)
}

fun Activity.bindOnClickLister(@IdRes vararg ids: Int, block: ((v: View) -> Unit)) {
    window?.decorView?.doBindOnClickLister(block.toOnClickLister(), ids)
}


fun Fragment.bindOnClickLister(listener: View.OnClickListener, @IdRes vararg ids: Int) {
    view?.doBindOnClickLister(listener, ids)
}

fun Fragment.bindOnClickLister(@IdRes vararg ids: Int, block: ((v: View) -> Unit)) {
    view?.doBindOnClickLister(block.toOnClickLister(), ids)
}

fun Dialog.bindOnClickLister(listener: View.OnClickListener, @IdRes vararg ids: Int) {
    window?.decorView?.doBindOnClickLister(listener, ids)
}

fun Dialog.bindOnClickLister(@IdRes vararg ids: Int, block: ((v: View) -> Unit)) {
    window?.decorView?.doBindOnClickLister(block.toOnClickLister(), ids)
}

private fun ((v: View) -> Unit).toOnClickLister(): View.OnClickListener {
    return View.OnClickListener { v -> invoke(v) }
}


fun View.OnClickListener.debounce(intervalMill: Int = 500): DebounceOnClickLister {
    return DebounceOnClickLister(this, intervalMill)
}


class DebounceOnClickLister @JvmOverloads constructor(
    private val listener: View.OnClickListener,
    private val intervalMill: Int = 500
) :
    View.OnClickListener {

    private var last = 0L
    override fun onClick(v: View) {
        if (System.currentTimeMillis() - last > intervalMill) {
            listener.onClick(v)
            last = System.currentTimeMillis()
        }
    }
}


fun View.setDebounceOnClickLister(block: ((v: View) -> Unit)) {
    setOnClickListener(block.toOnClickLister().debounce())
}

fun View.setDebounceOnClickLister(intervalMill: Int, block: ((v: View) -> Unit)) {
    setOnClickListener(block.toOnClickLister().debounce(intervalMill))
}

