package com.github.zchu.mvp

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

interface MvpPresenter : LifecycleOwner, ViewModelStoreOwner {

    @MainThread
    fun onViewInitialized()

}
