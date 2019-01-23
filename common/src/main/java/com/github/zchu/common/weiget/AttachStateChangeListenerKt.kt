package com.github.zchu.common.weiget

import android.view.View

class AttachStateChangeListenerBridge : View.OnAttachStateChangeListener {

    private var _onViewAttachedToWindow: ((view: View?) -> Unit)? = null
    private var _onViewDetachedFromWindow: ((view: View?) -> Unit)? = null

    fun _onViewAttachedToWindow(func: ((view: View?) -> Unit)) {
        _onViewAttachedToWindow = func
    }

    fun _onViewDetachedFromWindow(func: ((view: View?) -> Unit)) {
        _onViewDetachedFromWindow = func
    }

    override fun onViewAttachedToWindow(view: View?) {
        _onViewAttachedToWindow?.invoke(view)
    }

    override fun onViewDetachedFromWindow(view: View?) {
        _onViewDetachedFromWindow?.invoke(view)
    }
}

inline fun View._addOnAttachStateChangeListener(func: (AttachStateChangeListenerBridge.() -> Unit)) =
    addOnAttachStateChangeListener(AttachStateChangeListenerBridge().apply(func))
