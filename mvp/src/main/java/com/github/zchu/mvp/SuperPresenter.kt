package com.github.zchu.mvp

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import java.lang.ref.WeakReference
import java.util.*

class SuperPresenter<V : MvpView>(view: V) : MvpPresenter, StateListener {

    private val viewRef: WeakReference<V>?
    private var inState: Bundle? = null
    private var letInStateConsumers: LinkedList<((Bundle?) -> Unit)>? = null
    private var isCallOnRestoreState: Boolean = false


    private val lifecycle: Lifecycle = view.lifecycle

    private val viewModelStore: ViewModelStore = view.viewModelStore


    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    override val bundleKey: String
        get() {
            val view = view
            val viewTag = if (view != null) view.javaClass.name else "unknown"
            return viewTag + javaClass.name
        }


    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                this@SuperPresenter.onCreate()
            }

            override fun onStart(owner: LifecycleOwner) {
                this@SuperPresenter.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                this@SuperPresenter.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                this@SuperPresenter.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                this@SuperPresenter.onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                this@SuperPresenter.onDestroy()
            }
        })
        viewRef = WeakReference(view)
    }


    override fun onViewInitialized() {
        // do something
    }


    protected val view: V?
        get() = viewRef?.get()


    protected fun requireView(): V {
        return this.view ?: throw IllegalStateException("Presenter " + this + " is unbind View. View is null")
    }

    @MainThread
    protected fun onCreate() {

    }

    @MainThread
    protected fun onStart() {

    }

    @MainThread
    protected fun onResume() {

    }

    @MainThread
    protected fun onPause() {

    }

    @MainThread
    protected fun onStop() {

    }

    @MainThread
    @CallSuper
    protected fun onDestroy() {
        viewRef?.clear()
    }


    protected fun runOnViewNonNull(consumer: ((V) -> Unit)) {
        val v = view
        if (v != null) consumer.invoke(v)
    }

    protected fun runOnRestoreState(consumer2: ((Bundle?) -> Unit)) {
        if (isCallOnRestoreState) {
            consumer2.invoke(inState)
        } else {
            if (letInStateConsumers == null) {
                letInStateConsumers = LinkedList()
            }
            letInStateConsumers!!.add(consumer2)
        }
    }


    @CallSuper
    override fun onRestoreState(inState: Bundle?) {
        this.inState = inState
        this.isCallOnRestoreState = true
        val inStateConsumers = letInStateConsumers
        if (inStateConsumers != null) {
            inStateConsumers.forEach {
                it.invoke(inState)
            }
            inStateConsumers.clear()
        }
    }


    override fun onSaveState(outState: Bundle) {

    }
}
