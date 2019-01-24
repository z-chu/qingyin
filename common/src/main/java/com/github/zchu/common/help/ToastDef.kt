package com.github.zchu.common.help

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.github.zchu.common.util.requireNonNull
import com.github.zchu.common.util.whenNullDefault


@SuppressLint("StaticFieldLeak")
object ToastDef {

    private var toast: Toast? = null
    var defaultContext: Context? = null
        set(value) {
            field = value?.applicationContext
        }

    fun showShort(@StringRes resId: Int, context: Context? = null) {
        showShort(
            context.getString(resId)
            , context
        )
    }

    fun showShort(msg: String, context: Context? = null) {
        val toast = getToast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.setText(msg)
        toast.show()
    }

    fun showLong(@StringRes resId: Int, context: Context? = null) {
        showLong(
            context.getString(resId)
            , context
        )
    }

    fun showLong(msg: String, context: Context? = null) {
        val toast = getToast(context)
        toast.setText(msg)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    private fun Context?.getString(@StringRes resId: Int): String {
        return this
            .whenNullDefault(defaultContext.requireNonNull("At least one of Context and defaultContext is not null"))
            .getString(resId)
    }


    @SuppressLint("ShowToast")
    private fun getToast(context: Context?): Toast {
        return if (toast == null) {
            val contextLocal =
                if (context == null) {
                    defaultContext.requireNonNull("At least one of Context and defaultContext is not null")
                } else {
                    context.applicationContext
                }
            defaultContext = contextLocal


            val makeText = Toast.makeText(
                contextLocal,
                "",
                Toast.LENGTH_SHORT
            )
            toast = makeText


            makeText
        } else {
            toast!!
        }

    }

    fun cancel() {
        toast?.cancel()
    }

}

fun Context.showToastShort(@StringRes resId: Int) {
    showToastShort(this.getString(resId))
}

fun Context.showToastShort(msg: String) {
    ToastDef.showShort(msg, this)
}

fun showToastShort(msg: String) {
    ToastDef.showShort(msg)
}

fun Context.showToastLong(@StringRes resId: Int) {
    showToastLong(this.getString(resId))
}

fun Context.showToastLong(msg: String) {
    ToastDef.showLong(msg, this)
}

fun showToastLong(msg: String) {
    ToastDef.showLong(msg)
}

