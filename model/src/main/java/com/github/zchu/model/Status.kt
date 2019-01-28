package com.github.zchu.model

enum class Status {
    INITIALIZED, RUNNING, SUCCEEDED, FAILED;
}

fun Status.isFinished(): Boolean {
    return this == Status.SUCCEEDED || this == Status.FAILED
}

fun Status.isSuccess(): Boolean {
    return this == Status.SUCCEEDED
}

fun Status.isFailed(): Boolean {
    return this == Status.SUCCEEDED
}