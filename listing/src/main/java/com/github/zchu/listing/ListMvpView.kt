package com.github.zchu.listing

import com.github.zchu.mvp.MvpView


interface ListMvpView<T> : MvpView {

    fun showInitializing()

    fun showInitialized()

    fun showInitializationFailed(message: String?)

    fun showRefreshing()

    fun showRefreshed()

    fun showRefreshFailed(message: String?)

    fun showLoadingMore()

    fun showLoadMoreComplete()

    fun showLoadMoreFailed(message: String?)

    fun showNoMore()

    fun setInitial(initialData: List<T>)

    fun addMore(moreData: List<T>)

    fun setAll(allData: List<T>)


}