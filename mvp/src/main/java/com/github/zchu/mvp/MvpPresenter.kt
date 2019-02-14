package com.github.zchu.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface MvpPresenter2<V> {

    fun attach(view: V)

    fun detach()

    val view: V?
}

fun <V : MvpView, P : MvpPresenter2<V>> P.detachAt(lifecycleOwner: LifecycleOwner, untilEvent: Lifecycle.Event): P {
    return detachAt(lifecycleOwner.lifecycle, untilEvent)
}

fun <V : MvpView, P : MvpPresenter2<V>> P.detachAt(lifecycle: Lifecycle, untilEvent: Lifecycle.Event): P {
    if (untilEvent <= Lifecycle.Event.ON_RESUME || untilEvent == Lifecycle.Event.ON_ANY) {
        throw IllegalArgumentException("The parameter untilEvent($untilEvent) cannot be a positive event")
    }
    if (this.view == null) {
        return this
    }
    val currentState = lifecycle.currentState
    if (currentState == Lifecycle.State.DESTROYED) {
        this.detach()
    } else {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
                if (this@detachAt.view == null) {
                    source.lifecycle.removeObserver(this)
                } else {
                    if (event >= untilEvent) {
                        this@detachAt.detach()
                        source.lifecycle.removeObserver(this)
                    }
                }
            }
        })
    }

    return this
}

fun <V : MvpView, P : MvpPresenter2<V>> P.attachAndDetachAt(
    view: V,
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event
): P = attachAndDetachAt(view, lifecycleOwner.lifecycle, untilEvent)

fun <V : MvpView, P : MvpPresenter2<V>> P.attachAndDetachAt(
    view: V,
    lifecycle: Lifecycle,
    untilEvent: Lifecycle.Event
): P = view.attachToPresenter(this).detachAt(lifecycle, untilEvent)