package com.github.zchu.mvp

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner


open class ComponentPresenter2<V : MvpView>(
    private val lifecycle: Lifecycle,
    private val viewModelStore: ViewModelStore
) : SuperPresenter<V>(), LifecycleOwner, ViewModelStoreOwner, StateListener {

    constructor(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) : this(lifecycleOwner.lifecycle, viewModelStoreOwner.viewModelStore)


    constructor(
        activity: ComponentActivity
    ) : this(activity.lifecycle, activity.viewModelStore)

    constructor(
        fragment: Fragment
    ) : this(fragment.lifecycle, fragment.viewModelStore)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

}

fun <V : MvpView, P : ComponentPresenter2<V>> P.detachAt(untilEvent: Lifecycle.Event): P {
    return detachAt(lifecycle, untilEvent)
}


fun <V : MvpView, P : ComponentPresenter2<V>> P.attachAndDetachAt(
    view: V
): P = view.attachToPresenter(this).detachAt(lifecycle, Lifecycle.Event.ON_DESTROY)

fun <V : MvpView, P : ComponentPresenter2<V>> P.attachAndDetachAt(
    view: V,
    untilEvent: Lifecycle.Event
): P = view.attachToPresenter(this).detachAt(lifecycle, untilEvent)