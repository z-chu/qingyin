package com.github.zchu.listing

import com.github.zchu.mvp.MvpPresenter


interface ListMvpPresenter<T> : MvpPresenter {

    fun onResource(resource: ListResource<T>)

    fun refresh()

    fun retry()

    fun loadMore()

}