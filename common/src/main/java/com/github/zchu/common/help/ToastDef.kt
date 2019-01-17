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


    fun showShort(msg: String, context: Context? = null) {
        val toast = getToast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.setText(msg)
        toast.show()
    }

    fun showLong(msg: String, context: Context? = null) {
        val toast = getToast(context)
        toast.setText(msg)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    fun getToast(context: Context? = null): Toast {
        return toast.whenNullDefault(
            Toast.makeText(
                context.whenNullDefault(defaultContext.requireNonNull("At least one of Context and defaultContext is not null")),
                "",
                Toast.LENGTH_SHORT
            )
        )
    }

    fun cancel() {
        toast?.cancel()
    }

}

fun Context.toastShort(@StringRes resId: Int) {
    toastShort(this.getString(resId))
}

fun Context.toastShort(msg: String) {
    ToastDef.showShort(msg, this)
}

fun Context.toastLong(@StringRes resId: Int) {
    toastLong(this.getString(resId))
}

fun Context.toastLong(msg: String) {
    ToastDef.showLong(msg, this)
}

