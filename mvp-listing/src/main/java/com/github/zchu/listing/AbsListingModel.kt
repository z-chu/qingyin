package com.github.zchu.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class AbsListingModel<T> : ViewModel() {


    protected val refresh = { loadInitial(true) }

    protected val retry = { loadInitial(false) }

    protected val loadMore = { loadMore() }

    private var allData: MutableList<T> = ArrayList()

    private var isNoMore: Boolean = true


    private val listing: MutableLiveData<ListingResource<T>> by lazy {
        val mutableLiveData = MutableLiveData<ListingResource<T>>()
        mutableLiveData
    }

    fun getListing(): LiveData<ListingResource<T>> {
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
            listing.postValue(ListingResource.refreshing(ArrayList(allData), isNoMore))
        } else {
            listing.postValue(ListingResource.initializing())
        }
        val loadCallback = object : LoadCallback<T> {
            override fun onResult(data: List<T>) {

                if (isRefresh) {
                    allData.clear()
                    allData.addAll(data)
                    isNoMore = isNoMoreOnInitial(data)
                    listing.postValue(
                        ListingResource.refreshed(
                            data,
                            loadMore = if (isNoMore) null else loadMore,
                            ended = isNoMore,
                            refresh = refresh
                        )
                    )
                } else {
                    allData.addAll(data)
                    isNoMore = isNoMoreOnInitial(data)
                    listing.postValue(
                        ListingResource.initialized(
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
                    listing.postValue(ListingResource.refreshFailed(ArrayList(allData), t, refresh, loadMore, isNoMore))

                } else {
                    listing.postValue(ListingResource.initializationFailed(t, retry))
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
        listing.postValue(ListingResource.loadingMore(ArrayList(allData)))
        val loadCallback = object : LoadCallback<T> {
            override fun onResult(data: List<T>) {
                allData.addAll(data)
                isNoMore = isNoMoreOnMore(data)
                listing.postValue(
                    ListingResource.loadMoreComplete(
                        ArrayList(allData),
                        loadMore = if (isNoMore) null else loadMore
                        , ended = isNoMore,
                        moreData = data,
                        refresh = refresh
                    )
                )
            }

            override fun onFailure(t: Throwable) {
                listing.postValue(ListingResource.loadMoreFailed(ArrayList(allData), t, refresh, loadMore))
            }

        }
        doLoadMore(loadCallback)
    }

    protected open fun canLoadMore(): Boolean = true


    protected abstract fun doLoadMore(callback: LoadCallback<T>)


    protected abstract fun isNoMoreOnInitial(initialData: List<T>): Boolean

    protected abstract fun isNoMoreOnMore(moreData: List<T>): Boolean

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