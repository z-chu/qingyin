package com.github.zchu.listing

import com.github.zchu.mvp.MvpPresenter


interface ListingPresenter<T> : MvpPresenter {

    fun onResource(resource: ListingResource<T>)

    fun refresh()

    fun retry()

    fun loadMore()

}