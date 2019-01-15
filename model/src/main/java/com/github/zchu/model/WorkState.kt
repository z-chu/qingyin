package com.github.zchu.model


class WorkState private constructor(
    val status: Status,
    val throwable: Throwable? = null
) {
    companion object {
        val LOADED = WorkState(Status.SUCCESS)
        val LOADING = WorkState(Status.RUNNING)
        fun error(throwable: Throwable?) = WorkState(Status.FAILED, throwable)
    }
}