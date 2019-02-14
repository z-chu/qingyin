package com.github.zchu.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

open class LifecyclePresenter<V : MvpView>(
    private val lifecycle: Lifecycle
) : SuperPresenter<V>(), LifecycleOwner {

    constructor(
        lifecycleOwner: LifecycleOwner
    ) : this(lifecycleOwner.lifecycle)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}

fun <V : MvpView, P : LifecyclePresenter<V>> P.detachAt(untilEvent: Lifecycle.Event): P {
    return detachAt(lifecycle, untilEvent)
}

fun <V : MvpView, P : LifecyclePresenter<V>> P.attachAndDetachAt(
    view: V
): P = view.attachToPresenter(this).detachAt(lifecycle, Lifecycle.Event.ON_DESTROY)

fun <V : MvpView, P : LifecyclePresenter<V>> P.attachAndDetachAt(
    view: V,
    untilEvent: Lifecycle.Event
): P = view.attachToPresenter(this).detachAt(lifecycle, untilEvent)