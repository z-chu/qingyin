package com.github.zchu.listing

import com.github.zchu.mvp.SuperPresenter


open class ListPresenter<T>(view: ListMvpView<T>) : SuperPresenter<ListMvpView<T>>(view), ListMvpPresenter<T> {

    private var resource: ListResource<T>? = null
    private var initialData: List<T>? = null


    override fun onResource(resource: ListResource<T>) {
        val view = view ?: return
        this.resource = resource
        when {
            resource.action == ListResource.Action.INITIALIZE &&
                    resource.status == ListResource.Status.LOADING -> {
                onInitializing(view)
            }
            resource.action == ListResource.Action.INITIALIZE &&
                    resource.status == ListResource.Status.SUCCESS -> {
                onInitialized(view, resource)
            }
            resource.action == ListResource.Action.INITIALIZE &&
                    resource.status == ListResource.Status.ERROR -> {
                onInitializationFailed(view, resource)
            }
            resource.action == ListResource.Action.REFRESH &&
                    resource.status == ListResource.Status.LOADING -> {
                onRefreshing(view, resource)
            }
            resource.action == ListResource.Action.REFRESH &&
                    resource.status == ListResource.Status.SUCCESS -> {
                onRefreshed(view, resource)
            }
            resource.action == ListResource.Action.REFRESH &&
                    resource.status == ListResource.Status.ERROR -> {
                onRefreshFailed(view, resource)
            }
            resource.action == ListResource.Action.LOAD_MORE &&
                    resource.status == ListResource.Status.LOADING -> {
                onLoadingMore(view, resource)
            }
            resource.action == ListResource.Action.LOAD_MORE &&
                    resource.status == ListResource.Status.SUCCESS -> {
                onLoadMoreComplete(view, resource)
            }
            resource.action == ListResource.Action.LOAD_MORE &&
                    resource.status == ListResource.Status.ERROR -> {
                onLoadMoreFailed(view, resource)
            }
        }
    }

    protected open fun onInitializing(view: ListMvpView<T>) {
        view.showInitializing()
    }

    protected open fun onInitialized(view: ListMvpView<T>, resource: ListResource<T>) {
        setInitialData(view, resource)
        view.setAll(resource.all!!)
        view.showInitialized()
        checkEnded(view, resource)
    }

    protected open fun onInitializationFailed(view: ListMvpView<T>, resource: ListResource<T>) {
        view.showInitializationFailed(handleException(resource.throwable))
    }

    protected open fun onRefreshing(view: ListMvpView<T>, resource: ListResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshing()
        checkEnded(view, resource)
    }

    protected open fun onRefreshed(view: ListMvpView<T>, resource: ListResource<T>) {
        setInitialData(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshed()
        checkEnded(view, resource)
    }

    protected open fun onRefreshFailed(view: ListMvpView<T>, resource: ListResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showRefreshFailed(handleException(resource.throwable))
        checkEnded(view, resource)
    }

    protected open fun onLoadingMore(view: ListMvpView<T>, resource: ListResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showLoadingMore()
    }

    protected open fun onLoadMoreComplete(view: ListMvpView<T>, resource: ListResource<T>) {
        if (checkInitialNoNull(view, resource) && resource.more!!.isNotEmpty()) {
            view.addMore(resource.more)
        }
        view.setAll(resource.all!!)
        view.showLoadMoreComplete()
        checkEnded(view, resource)
    }

    protected open fun onLoadMoreFailed(view: ListMvpView<T>, resource: ListResource<T>) {
        checkInitialNoNull(view, resource)
        view.setAll(resource.all!!)
        view.showLoadMoreFailed(handleException(resource.throwable))
    }

    private fun checkInitialNoNull(view: ListMvpView<T>, resource: ListResource<T>): Boolean {
        if (initialData == null) {
            setInitialData(view, resource)
            return false
        }
        return true
    }

    private fun setInitialData(view: ListMvpView<T>, resource: ListResource<T>) {
        initialData = resource.all
        view.setInitial(resource.all!!)
        view.showInitialized()
    }

    private fun checkEnded(view: ListMvpView<T>, resource: ListResource<T>): Boolean {
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
