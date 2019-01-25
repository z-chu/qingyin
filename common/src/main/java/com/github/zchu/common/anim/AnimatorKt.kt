package com.github.zchu.common.anim

import android.animation.Animator
import android.view.ViewPropertyAnimator

class AnimatorListenerBridge : Animator.AnimatorListener {

    private var _onAnimationStart: ((animation: Animator?) -> Unit)? = null
    private var _onAnimationEnd: ((animation: Animator?) -> Unit)? = null
    private var _onAnimationCancel: ((animation: Animator?) -> Unit)? = null
    private var _onAnimationRepeat: ((animation: Animator?) -> Unit)? = null

    fun _onAnimationStart(t: ((animation: Animator?) -> Unit)) {
        _onAnimationStart = t
    }

    fun _onAnimationEnd(t: ((animation: Animator?) -> Unit)) {
        _onAnimationEnd = t
    }

    fun _onAnimationCancel(t: ((animation: Animator?) -> Unit)) {
        _onAnimationCancel = t
    }

    fun _onAnimationRepeat(t: ((animation: Animator?) -> Unit)) {
        _onAnimationRepeat = t
    }

    override fun onAnimationStart(animation: Animator?) {
        _onAnimationStart?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animator?) {
        _onAnimationEnd?.invoke(animation)
    }

    override fun onAnimationCancel(animation: Animator?) {
        _onAnimationCancel?.invoke(animation)
    }

    override fun onAnimationRepeat(animation: Animator?) {
        _onAnimationRepeat?.invoke(animation)
    }
}

inline fun ViewPropertyAnimator._setListener(func: (AnimatorListenerBridge.() -> Unit)) =
    setListener(AnimatorListenerBridge().apply(func))

inline fun Animator._addListener(func: (AnimatorListenerBridge.() -> Unit)) =
    addListener(AnimatorListenerBridge().apply(func))
