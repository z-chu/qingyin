package com.github.zchu.model


class ViewData<T> private constructor(
    val workState: WorkState,
    val value: T? = null
) {
    companion object {

        @JvmOverloads
        fun <T> loading(value: T? = null) = ViewData(WorkState.LOADING, value)

        fun <T> loaded(value: T) = ViewData(WorkState.LOADED, value)

        @JvmOverloads
        fun <T> error(throwable: Throwable?, value: T? = null) = ViewData(WorkState.error(throwable), value)
    }
}

fun <T> ViewData<T>.whenRun(block: ViewDataBridge<T>.() -> Unit) {
    val bridge = ViewDataBridge<T>().apply(block)
    when (workState.status) {
        Status.RUNNING -> bridge.onLoading(value)
        Status.FAILED -> bridge.onError(workState.throwable, value)
        Status.SUCCEEDED -> bridge.onSuccess(value!!)
    }
}

class ViewDataBridge<T> {

    private var _onLoading: ((value: T?) -> Unit)? = null

    private var _onSuccess: ((value: T) -> Unit)? = null

    private var _onError: ((throwable: Throwable?, value: T?) -> Unit)? = null

    fun onLoading(block: ((value: T?) -> Unit)) {
        _onLoading = block
    }

    fun onSuccess(block: ((value: T) -> Unit)) {
        _onSuccess = block
    }

    fun onError(block: ((throwable: Throwable?, value: T?) -> Unit)) {
        _onError = block

    }

    internal fun onLoading(value: T?) {
        _onLoading?.invoke(value)
    }

    internal fun onSuccess(value: T) {
        _onSuccess?.invoke(value)
    }

    internal fun onError(throwable: Throwable?, value: T?) {
        _onError?.invoke(throwable, value)
    }

}