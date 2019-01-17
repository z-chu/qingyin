package com.github.zchu.common.util


fun <T : Any> T?.requireNonNull(message: String? = null): T {
    if (this == null) {
        throw NullPointerException(message)
    }
    return this
}


fun <T : Any> T?.whenNullDefault(defaultValue: T): T {
    return this ?: defaultValue
}