package com.github.zchu.listing

data class ListResource<T>(
    val status: Status,
    val action: Action,
    val all: List<T>? = null,
    val more: List<T>? = null,
    val ended: Boolean = true,
    val refresh: (() -> Unit)? = null,
    val retry: (() -> Unit)? = null,
    val loadMore: (() -> Unit)? = null,
    val throwable: Throwable? = null
) {


    fun toRefreshing(): ListResource<T> {
        return refreshing(all!!, ended)
    }

    fun toLoadingMore(): ListResource<T> {
        return loadingMore(all!!)
    }

    companion object {

        fun <T> initializing(): ListResource<T> {
            return ListResource(Status.LOADING, Action.INITIALIZE)
        }

        fun <T> initialized(
            initialData: List<T>,
            refresh: (() -> Unit)? = null,
            loadMore: (() -> Unit)? = null,
            ended: Boolean = true
        ): ListResource<T> {
            return ListResource(
                Status.SUCCESS, Action.INITIALIZE, initialData,
                refresh = refresh, loadMore = loadMore, ended = ended
            )
        }

        fun <T> initializationFailed(throwable: Throwable?, retry: (() -> Unit)? = null): ListResource<T> {
            return ListResource(Status.ERROR, Action.INITIALIZE, retry = retry, throwable = throwable)
        }

        fun <T> refreshing(allData: List<T>, ended: Boolean = true): ListResource<T> {
            return ListResource(Status.LOADING, Action.REFRESH, all = allData, ended = ended)
        }

        fun <T> refreshed(
            newData: List<T>,
            refresh: (() -> Unit),
            loadMore: (() -> Unit)? = null,
            ended: Boolean = true
        ): ListResource<T> {
            return ListResource(
                Status.SUCCESS, Action.REFRESH, newData,
                refresh = refresh, loadMore = loadMore, ended = ended
            )
        }

        fun <T> refreshFailed(
            oldData: List<T>, throwable: Throwable?,
            refresh: (() -> Unit),
            loadMore: (() -> Unit)? = null,
            ended: Boolean = true
        ): ListResource<T> {
            return ListResource(
                Status.ERROR, Action.REFRESH, oldData, throwable = throwable,
                refresh = refresh, loadMore = loadMore, ended = ended
            )
        }

        fun <T> loadingMore(allData: List<T>): ListResource<T> {
            return ListResource(Status.LOADING, Action.LOAD_MORE, allData, ended = false)
        }

        fun <T> loadMoreComplete(
            newAllData: List<T>, moreData: List<T>,
            refresh: (() -> Unit)? = null,
            loadMore: (() -> Unit)? = null,
            ended: Boolean = true
        ): ListResource<T> {
            return ListResource(
                Status.SUCCESS, Action.LOAD_MORE, newAllData, moreData,
                refresh = refresh, loadMore = loadMore, ended = ended
            )
        }

        fun <T> loadMoreFailed(
            oldData: List<T>, throwable: Throwable?,
            refresh: (() -> Unit)? = null,
            loadMore: (() -> Unit)? = null
        ): ListResource<T> {
            return ListResource(
                Status.ERROR, Action.LOAD_MORE, oldData,
                throwable = throwable, refresh = refresh, loadMore = loadMore, ended = false
            )
        }
    }

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    enum class Action {
        INITIALIZE, REFRESH, LOAD_MORE
    }

}
