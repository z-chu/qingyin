package com.github.zchu.listing

import com.github.zchu.mvp.SuperPresenter


open class SuperListingPresenter<T>(view: ListingView<T>) : SuperPresenter<ListingView<T>>(view), ListingPresenter<T> {

    private var resource: ListingResource<T>? = null
    private var initialData: List<T>? = null


    override fun onResource(resource: ListingResource<T>) {
        val view = view ?: return
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

    protected open fun onInitializing(view: ListingView<T>) {
        view.showInitializing()
    }

    protected open fun onInitialized(view: ListingView<T>, resource: ListingResource<T>) {
        setInitialData(view, resource)
        view.setAll(resource.all!!)
        view.showInitialized()
        checkEnded(view, resource)
    }

    protected open fun onInitializationFailed(view: ListingView<T>, resource: ListingResource<T>) {
        view.showInitializationFailed(handleException(resource.throwable))
    }

    protected open fun onRefreshing(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshing()
        checkEnded(view, resource)
    }

    protected open fun onRefreshed(view: ListingView<T>, resource: ListingResource<T>) {
        setInitialData(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshed()
        checkEnded(view, resource)
    }

    protected open fun onRefreshFailed(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshFailed(handleException(resource.throwable))
        checkEnded(view, resource)
    }

    protected open fun onLoadingMore(view: ListingView<T>, resource: ListingResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showLoadingMore()
    }

    protected open fun onLoadMoreComplete(view: ListingView<T>, resource: ListingResource<T>) {
        if (checkInitialNoNull(view, resource) && resource.more!!.isNotEmpty()) {
            view.addMore(resource.more)
        }
        view.setAll(resource.all!!)
        view.showLoadMoreComplete()
        checkEnded(view, resource)
    }

    protected open fun onLoadMoreFailed(view: ListingView<T>, resource: ListingResource<T>) {
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


    override fun refresh() {
        resource?.refresh?.invoke()
    }

    override fun retry() {
        resource?.retry?.invoke()
    }

    override fun loadMore() {
        resource?.loadMore?.invoke()
    }


    protected open fun handleException(throwable: Throwable?): String? {
        return throwable?.message
    }


}
