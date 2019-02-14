package com.github.zchu.mvp

import android.os.Bundle
import androidx.annotation.CallSuper
import java.lang.ref.WeakReference
import java.util.*


open class SuperPresenter<V : MvpView> : MvpPresenter<V>, StateListener {

    private var viewRef: WeakReference<V>? = null
    private var inState: Bundle? = null
    private var letInStateConsumers: LinkedList<((Bundle?) -> Unit)>? = null
    private var isCallOnRestoreState: Boolean = false


    override fun attach(view: V) {
        if (this.view != null) {
            throw IllegalStateException("Already attached to the view.")
        }
        viewRef = WeakReference(view)
    }

    override fun detach() {
        (viewRef ?: throw IllegalStateException("Presenter $this not attached view.")).clear()
    }

    override val view: V?
        get() = viewRef?.get()

    protected fun requireView(): V {
        return this.view ?: throw IllegalStateException("Presenter $this not attached view. View is null.")
    }

    fun isViewAttached(): Boolean = view != null

    override val bundleKey: String
        get() {
            val view = view
            val viewTag = if (view != null) view.javaClass.name else "unknown"
            return viewTag + javaClass.name
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
