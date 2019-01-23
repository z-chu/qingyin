package com.github.zchu.common.rx

import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

class FlowableBridge<T> : FlowableSubscriber<T> {

    private var _onSubscribe: ((s: Subscription) -> Unit)? = null
    private var _onNext: ((t: T) -> Unit)? = null
    private var _onComplete: (() -> Unit)? = null
    private var _onError: ((e: Throwable) -> Unit)? = null

    fun _onSubscribe(t: ((s: Subscription) -> Unit)) {
        _onSubscribe = t
    }

    fun _onNext(t: ((t: T) -> Unit)) {
        _onNext = t
    }

    fun _onComplete(t: (() -> Unit)) {
        _onComplete = t
    }

    fun _onError(t: ((e: Throwable) -> Unit)) {
        _onError = t
    }

    override fun onSubscribe(s: Subscription) {
        _onSubscribe?.invoke(s)
    }

    override fun onNext(t: T) {
        _onNext?.invoke(t)
    }

    override fun onComplete() {
        _onComplete?.invoke()
    }

    override fun onError(e: Throwable) {
        _onError?.invoke(e)
    }
}

inline fun <reified T> Flowable<T>._subscribe(func: FlowableBridge<T>.() -> Unit) =
    subscribe(FlowableBridge<T>().apply(func))