package com.github.zchu.bridge

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

fun Lifecycle.addObserver(block: LifecycleObserverBridge.() -> Unit) =
    addObserver(LifecycleObserverBridge().apply(block))

class LifecycleObserverBridge : LifecycleObserver {
    private var _onCreate: ((source: LifecycleOwner) -> Unit)? = null
    private var _onStart: ((source: LifecycleOwner) -> Unit)? = null
    private var _onResume: ((source: LifecycleOwner) -> Unit)? = null
    private var _onPause: ((source: LifecycleOwner) -> Unit)? = null
    private var _onStop: ((source: LifecycleOwner) -> Unit)? = null
    private var _onDestroy: ((source: LifecycleOwner) -> Unit)? = null

    @Suppress("NON_EXHAUSTIVE_WHEN")
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> _onCreate?.invoke(source)
            Lifecycle.Event.ON_START -> _onStart?.invoke(source)
            Lifecycle.Event.ON_RESUME -> _onResume?.invoke(source)
            Lifecycle.Event.ON_PAUSE -> _onPause?.invoke(source)
            Lifecycle.Event.ON_STOP -> _onStop?.invoke(source)
            Lifecycle.Event.ON_DESTROY -> _onDestroy?.invoke(source)
        }
    }

    fun onCreated(block: (source: LifecycleOwner) -> Unit) {
        _onCreate = block
    }

    fun onStarted(block: (source: LifecycleOwner) -> Unit) {
        _onStart = block
    }

    fun onResumed(block: (source: LifecycleOwner) -> Unit) {
        _onResume = block
    }

    fun onPaused(block: (source: LifecycleOwner) -> Unit) {
        _onPause = block
    }

    fun onStopped(block: (source: LifecycleOwner) -> Unit) {
        _onStop = block
    }

    fun onDestroyed(block: (source: LifecycleOwner) -> Unit) {
        _onDestroy = block
    }
}