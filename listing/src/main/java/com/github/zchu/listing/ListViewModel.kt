package com.github.zchu.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ListViewModel<T> : ViewModel() {


    protected val refresh = { loadInitial(true) }

    protected val retry = { loadInitial(false) }

    protected val loadMore = { loadMore() }

    private var allData: MutableList<T> = ArrayList()

    private var isNoMore: Boolean = true


    private val listing: MutableLiveData<ListResource<T>> by lazy {
        val mutableLiveData = MutableLiveData<ListResource<T>>()
        mutableLiveData
    }

    fun getListing(): LiveData<ListResource<T>> {
        if (listing.value == null) {
            loadInitial(false)
        }
        return listing
    }

    protected open fun loadInitial(isRefresh: Boolean) {
        if (!canLoadInitial(isRefresh)) {
            return
        }
        if (isRefresh) {
            listing.postValue(ListResource.refreshing(ArrayList(allData), isNoMore))
        } else {
            listing.postValue(ListResource.initializing())
        }
        val loadCallback = object : LoadCallback<T> {
            override fun onResult(data: List<T>) {
                allData.addAll(data)
                isNoMore = isNoMoreOnInitial(data)
                if (isRefresh) {
                    listing.postValue(
                        ListResource.refreshed(
                            data,
                            loadMore = if (isNoMore) null else loadMore,
                            ended = isNoMore,
                            refresh = refresh
                        )
                    )
                } else {
                    listing.postValue(
                        ListResource.initialized(
                            data,
                            loadMore = if (isNoMore) null else loadMore,
                            ended = isNoMore,
                            refresh = refresh
                        )
                    )
                }
            }

            override fun onFailure(t: Throwable) {
                if (isRefresh) {
                    listing.postValue(ListResource.refreshFailed(ArrayList(allData), t, refresh, loadMore, isNoMore))

                } else {
                    listing.postValue(ListResource.initializationFailed(t, retry))
                }
            }

        }

        doLoadInitial(isRefresh, loadCallback)

    }

    protected open fun canLoadInitial(isRefresh: Boolean): Boolean = true

    protected abstract fun doLoadInitial(isRefresh: Boolean, callback: LoadCallback<T>)

    protected open fun loadMore() {
        if (!canLoadMore()) {
            return
        }
        listing.postValue(ListResource.loadingMore(ArrayList(allData)))
        val loadCallback = object : LoadCallback<T> {
            override fun onResult(data: List<T>) {
                allData.addAll(data)
                isNoMore = isNoMoreOnMore(data)
                listing.postValue(
                    ListResource.loadMoreComplete(
                        ArrayList(allData),
                        loadMore = if (isNoMore) null else loadMore
                        , ended = isNoMore,
                        moreData = data,
                        refresh = refresh
                    )
                )
            }

            override fun onFailure(t: Throwable) {
                listing.postValue(ListResource.loadMoreFailed(ArrayList(allData), t, refresh, loadMore))
            }

        }
        doLoadMore(loadCallback)
    }

    protected open fun canLoadMore(): Boolean = true


    protected abstract fun doLoadMore(callback: LoadCallback<T>)


    protected abstract fun isNoMoreOnInitial(data: List<T>): Boolean

    protected abstract fun isNoMoreOnMore(data: List<T>): Boolean

    protected fun getData(): List<T> {
        return allData
    }

    protected fun getDataSize(): Int {
        return allData.size
    }

    interface LoadCallback<T> {

        fun onResult(data: List<T>)

        fun onFailure(t: Throwable)
    }

}