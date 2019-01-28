package com.github.zchu.common.util


fun <T : Any> T?.requireNonNull(message: String? = null): T {
    if (this == null) {
        throw NullPointerException(message)
    }
    return this
}

fun checkNonNull(vararg objects: Any?): Boolean {
    for (`object` in objects) {
        if (`object` == null) {
            return false
        }
    }
    return true
}


fun <T : Any> T?.whenNullDef(defaultValue: T): T {
    return this ?: defaultValue
}

inline fun <T : Any> T?.whenNullDef(defaultValueFuc: () -> T): T {
    return this ?: defaultValueFuc.invoke()
}