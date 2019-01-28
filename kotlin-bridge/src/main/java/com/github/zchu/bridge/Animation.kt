package com.github.zchu.bridge

import android.animation.Animator
import android.view.ViewPropertyAnimator
import android.view.animation.Animation

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


class AnimationListenerBridge : Animation.AnimationListener {

    private var _onAnimationStart: ((animation: Animation?) -> Unit)? = null
    private var _onAnimationEnd: ((animation: Animation?) -> Unit)? = null
    private var _onAnimationRepeat: ((animation: Animation?) -> Unit)? = null

    fun onAnimationStart(func: (animation: Animation?) -> Unit) {
        _onAnimationStart = func
    }

    fun onAnimationEnd(func: (animation: Animation?) -> Unit) {
        _onAnimationEnd = func
    }

    fun onAnimationRepeat(func: (animation: Animation?) -> Unit) {
        _onAnimationRepeat = func
    }

    override fun onAnimationStart(animation: Animation?) {
        _onAnimationStart?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animation?) {
        _onAnimationEnd?.invoke(animation)

    }

    override fun onAnimationRepeat(animation: Animation?) {
        _onAnimationRepeat?.invoke(animation)
    }
}

inline fun Animation._setAnimationListener(func: (AnimationListenerBridge.() -> Unit)) =
    setAnimationListener(AnimationListenerBridge().apply(func))