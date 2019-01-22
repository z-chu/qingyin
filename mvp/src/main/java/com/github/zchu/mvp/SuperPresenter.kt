package com.github.zchu.mvp

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import java.lang.ref.WeakReference
import java.util.*


open class SuperPresenter<V : MvpView>(view: V) : MvpPresenter, StateListener {

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
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreate()
                    Lifecycle.Event.ON_START -> onStart()
                    Lifecycle.Event.ON_RESUME -> onResume()
                    Lifecycle.Event.ON_PAUSE -> onPause()
                    Lifecycle.Event.ON_STOP -> onStop()
                    Lifecycle.Event.ON_DESTROY -> onDestroy()
                    else -> {
                        //do nothing
                    }
                }
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
    protected open fun onCreate() {

    }

    @MainThread
    protected open fun onStart() {

    }

    @MainThread
    protected open fun onResume() {

    }

    @MainThread
    protected open fun onPause() {

    }

    @MainThread
    protected open fun onStop() {

    }

    @MainThread
    @CallSuper
    protected open fun onDestroy() {
        viewRef?.clear()
    }


    protected inline fun runOnViewNonNull(consumer: ((V) -> Unit)) {
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
