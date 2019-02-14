package com.github.zchu.mvp

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

open class ComponentPresenter<V : MvpView>(
    lifecycle: Lifecycle,
    private val viewModelStore: ViewModelStore
) : LifecyclePresenter<V>(lifecycle), ViewModelStoreOwner {

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


    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

}
