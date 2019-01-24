package com.github.zchu.common.help

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat
import com.github.zchu.common.util.requireNonNull
import com.github.zchu.common.util.whenNullDefault
import java.lang.ref.WeakReference


@SuppressLint("StaticFieldLeak")
object ToastDef {

    private var defaultToast: Toast? = null
    private var prevToastCompat: WeakReference<ToastCompat>? = null

    var defaultContext: Context? = null
        set(value) {
            if (field == null) {
                field = value?.applicationContext
            }
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
        show(toast, context)
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
        show(toast, context)
    }

    private fun show(toast: Toast, context: Context?) {
        if (context == null || isNotificationEnabled(context)) {
            toast.show()
        } else {
            if (context is Activity) {
                prevToastCompat?.get()?.cancel()
                val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val toastCompat = ToastCompat(toast, windowManager)
                toastCompat.show()
                prevToastCompat = WeakReference(toastCompat)
            }
        }
    }

    private fun Context?.getString(@StringRes resId: Int): String {
        return this
            .whenNullDefault(defaultContext.requireNonNull("At least one of Context and defaultContext is not null"))
            .getString(resId)
    }


    @SuppressLint("ShowToast")
    fun getToast(context: Context? = null): Toast {
        defaultContext = context
        return defaultToast.whenNullDefault {
            val makeText = Toast.makeText(
                defaultContext.requireNonNull("At least one of Context and defaultContext is not null"),
                "",
                Toast.LENGTH_SHORT
            )
            defaultToast = makeText
            makeText
        }
    }


    fun cancel() {
        defaultToast?.cancel()
        prevToastCompat?.get()?.cancel()
    }

    @JvmStatic
    private fun isNotificationEnabled(context: Context): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        return notificationManagerCompat.areNotificationsEnabled()
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

