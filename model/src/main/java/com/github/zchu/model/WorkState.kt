package com.github.zchu.model


class WorkState private constructor(
    val status: Status,
    val throwable: Throwable? = null
) {
    companion object {

        val LOADING = WorkState(Status.RUNNING)

        val LOADED = WorkState(Status.SUCCEEDED)

        fun error(throwable: Throwable?) = WorkState(Status.FAILED, throwable)
    }
}