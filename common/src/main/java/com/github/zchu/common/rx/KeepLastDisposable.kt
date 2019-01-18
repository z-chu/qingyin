package com.github.zchu.common.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import java.util.*

class KeepLastDisposable
private constructor(private val compositeDisposable: CompositeDisposable) : Disposable by compositeDisposable,
    DisposableContainer by compositeDisposable {

    constructor() : this(CompositeDisposable())

    private val disposableMap = HashMap<String, Disposable>()

    fun add(key: String, d: Disposable) {
        val oldDisposable = disposableMap[key]
        if (oldDisposable != null && !oldDisposable.isDisposed) {
            oldDisposable.dispose()
        }
        disposableMap[key] = d
        compositeDisposable.add(d)
    }

    fun get(key: String): Disposable? {
        return disposableMap[key]

    }

    fun remove(key: String) {
        val disposable = disposableMap[key]
        if (disposable != null) {
            compositeDisposable.remove(disposable)
        }
    }

    override fun dispose() {
        compositeDisposable.dispose()
        disposableMap.clear()
    }


}