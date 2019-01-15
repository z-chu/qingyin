package com.github.zchu.model


class ViewData<T> private constructor(
    val workState: WorkState,
    val value: T? = null
) {
    companion object {
        fun <T> loaded(value: T) = ViewData<Any>(WorkState.LOADED, value)
        fun <T> loading() = ViewData<T>(WorkState.LOADING)
        fun <T> loading(value: T?) = ViewData<T>(WorkState.LOADING, value)
        fun <T> error(throwable: Throwable?) = ViewData<T>(WorkState.error(throwable))
        fun <T> error(throwable: Throwable?, value: T?) = ViewData<T>(WorkState.error(throwable), value)
    }
}