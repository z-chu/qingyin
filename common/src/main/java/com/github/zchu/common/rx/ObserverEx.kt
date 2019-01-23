package com.github.zchu.common.rx

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ObserverBridge<T> : Observer<T> {

    private var _onSubscribe: ((disposable: Disposable) -> Unit)? = null
    private var _onNext: ((t: T) -> Unit)? = null
    private var _onComplete: (() -> Unit)? = null
    private var _onError: ((e: Throwable) -> Unit)? = null
    private lateinit var disposable: Disposable

    fun _onSubscribe(t: ((disposable: Disposable) -> Unit)) {
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

    fun getDisposable(): Disposable {
        return disposable
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        _onSubscribe?.invoke(d)
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

inline fun <reified T> Observable<T>._subscribe(func: ObserverBridge<T>.() -> Unit): Disposable {
    val real = ObserverBridge<T>()
    real.func()
    subscribe(real)
    return real.getDisposable()
}