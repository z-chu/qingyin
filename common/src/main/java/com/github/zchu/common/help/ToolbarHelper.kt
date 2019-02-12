package com.github.zchu.common.help

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.github.zchu.common.util.findViewByClass

@JvmOverloads
fun AppCompatActivity.initToolbar(
    title: CharSequence? = null,
    canBack: Boolean = true,
    @IdRes toolbarId: Int? = null
): Toolbar {
    val toolbar = if (toolbarId == null) {
        window.decorView.findViewByClass(Toolbar::class.java)
    } else {
        findViewById(toolbarId)
    } ?: throw IllegalStateException(
        "The AppCompatActivity must contain a toolbar."
    )
    toolbar.setToSupportActionBar(this, title, canBack)
    return toolbar
}


private fun Toolbar.setToSupportActionBar(
    activity: AppCompatActivity,
    title: CharSequence?,
    canBack: Boolean
) {
    activity.setSupportActionBar(this)
    val actionBar = activity.supportActionBar
    if (actionBar != null) {
        if (canBack) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { activity.onBackPressed() }
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false)
            setNavigationOnClickListener(null)
        }
        actionBar.title = title
        if (title != null) {
            actionBar.setDisplayShowTitleEnabled(true)
        } else {
            actionBar.setDisplayShowTitleEnabled(false)
        }
    } else {
        if (canBack) {
            setNavigationOnClickListener { activity.onBackPressed() }
        } else {
            setNavigationOnClickListener(null)
        }
        this.title = title
    }
}

@JvmOverloads
fun Fragment.initToolbar(
    title: CharSequence? = null,
    canBack: Boolean = true, @IdRes toolbarId: Int? = null
): Toolbar {
    val toolbar = if (toolbarId == null) {
        view!!.findViewByClass(Toolbar::class.java)
    } else {
        view!!.findViewById(toolbarId)
    } ?: throw IllegalStateException(
        "The fragment must contain a toolbar."
    )
    val activity = activity
    if (activity != null && activity is AppCompatActivity) {
        toolbar.setToSupportActionBar(activity, title, canBack)
    } else {
        if (canBack) {
            toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        } else {
            toolbar.setNavigationOnClickListener(null)
        }
        toolbar.title = title
    }
    return toolbar
}
