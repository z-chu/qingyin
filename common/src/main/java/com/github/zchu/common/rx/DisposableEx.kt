package com.github.zchu.common.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.bindLifecycle(lifecycleOwner: LifecycleOwner) {
    bindLifecycle(lifecycleOwner.lifecycle)
}

fun Disposable.bindLifecycle(lifecycle: Lifecycle) {
    bindLifecycle(lifecycle, getOpponentLifecycleEvent(lifecycle))
}

fun Disposable.bindLifecycle(lifecycle: Lifecycle, untilEvent: Lifecycle.Event) {
    val currentState = lifecycle.currentState
    if (currentState == Lifecycle.State.DESTROYED) {
        this.dispose()
    } else {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event > Lifecycle.Event.ON_RESUME && event >= untilEvent) {
                    this@bindLifecycle.dispose()
                }
            }
        })
    }

}

private fun getOpponentLifecycleEvent(lifecycle: Lifecycle): Lifecycle.Event {
    return when (lifecycle.currentState) {
        Lifecycle.State.RESUMED -> Lifecycle.Event.ON_PAUSE
        Lifecycle.State.STARTED -> Lifecycle.Event.ON_STOP
        else -> Lifecycle.Event.ON_DESTROY
    }
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}