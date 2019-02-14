package com.github.zchu.listing

import androidx.annotation.CallSuper
import androidx.lifecycle.Observer

class ListingObserver<T>(private val view: ListingView<T>, var errorFunc: ((Throwable) -> String)? = null) :
    Observer<ListingResource<T>> {

    var resource: ListingResource<T>? = null
    private var initialData: List<T>? = null


    @CallSuper
    override fun onChanged(resource: ListingResource<T>?) {
        resource ?: return
        this.resource = resource
        when {
            resource.action == ListingResource.Action.INITIALIZE &&
                    resource.status == ListingResource.Status.LOADING -> {
                onInitializing(view)
            }
            resource.action == ListingResource.Action.INITIALIZE &&
                    resource.status == ListingResource.Status.SUCCESS -> {
                onInitialized(view, resource)
            }
            resource.action == ListingResource.Action.INITIALIZE &&
                    resource.status == ListingResource.Status.ERROR -> {
                onInitializationFailed(view, resource)
            }
            resource.action == ListingResource.Action.REFRESH &&
                    resource.status == ListingResource.Status.LOADING -> {
                onRefreshing(view, resource)
            }
            resource.action == ListingResource.Action.REFRESH &&
                    resource.status == ListingResource.Status.SUCCESS -> {
                onRefreshed(view, resource)
            }
            resource.action == ListingResource.Action.REFRESH &&
                    resource.status == ListingResource.Status.ERROR -> {
                onRefreshFailed(view, resource)
            }
            resource.action == ListingResource.Action.LOAD_MORE &&
                    resource.status == ListingResource.Status.LOADING -> {
                onLoadingMore(view, resource)
            }
            resource.action == ListingResource.Action.LOAD_MORE &&
                    resource.status == ListingResource.Status.SUCCESS -> {
                onLoadMoreComplete(view, resource)
            }
            resource.action == ListingResource.Action.LOAD_MORE &&
                    resource.status == ListingResource.Status.ERROR -> {
                onLoadMoreFailed(view, resource)
            }
        }
    }

    private fun onInitializing(view: ListingView<T>) {
        view.showInitializing()
    }

    private fun onInitialized(view: ListingView<T>, resource: ListingResource<T>) {
        setInitialData(view, resource)
        view.setAll(resource.all!!)
        checkEnded(view, resource)
    }

    private fun onInitializationFailed(view: ListingView<T>, resource: ListingResource<T>) {
        view.showInitializationFailed(handleException(resource.throwable))
    }

    private fun onRefreshing(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshing()
        checkEnded(view, resource)
    }

    private fun onRefreshed(view: ListingView<T>, resource: ListingResource<T>) {
        setInitialData(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshed()
        checkEnded(view, resource)
    }

    private fun onRefreshFailed(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshFailed(handleException(resource.throwable))
        checkEnded(view, resource)
    }

    private fun onLoadingMore(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showLoadingMore()
    }

    private fun onLoadMoreComplete(view: ListingView<T>, resource: ListingResource<T>) {
        if (checkInitialNoNull(view, resource) && resource.more!!.isNotEmpty()) {
            view.addMore(resource.more)
        }
        view.setAll(resource.all!!)
        view.showLoadMoreComplete()
        checkEnded(view, resource)
    }

    private fun onLoadMoreFailed(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showLoadMoreFailed(handleException(resource.throwable))
    }

    private fun checkInitialNoNull(view: ListingView<T>, resource: ListingResource<T>): Boolean {
        if (initialData == null) {
            setInitialData(view, resource)
            return false
        }
        return true
    }

    private fun setInitialData(view: ListingView<T>, resource: ListingResource<T>) {
        initialData = resource.all
        view.setInitial(resource.all!!)
        view.showInitialized()
    }

    private fun checkEnded(view: ListingView<T>, resource: ListingResource<T>): Boolean {
        if (resource.ended) {
            view.showNoMore()
            return true
        }
        return false
    }

    fun refresh() {
        resource?.refresh?.invoke()
    }

    fun retry() {
        resource?.retry?.invoke()
    }

    fun loadMore() {
        resource?.loadMore?.invoke()
    }

    private fun handleException(throwable: Throwable?): String? {
        return throwable?.let {
            errorFunc?.invoke(it) ?: it.message
        }
    }

}