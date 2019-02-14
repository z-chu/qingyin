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


fun <T : Any> T?.defaultIfNull(defaultValue: T): T {
    return this ?: defaultValue
}

inline fun <T : Any> T?.defaultIfNull(defaultValueFuc: () -> T): T {
    return this ?: defaultValueFuc.invoke()
}


fun <T> ((T) -> Unit).debounce(intervalMill: Int = 500): (T) -> Unit {
    return object : ((T) -> Unit) {
        var last = 0L
        override fun invoke(t: T) {
            if (System.currentTimeMillis() - last > intervalMill) {
                this@debounce.invoke(t)
                last = System.currentTimeMillis()
            }
        }
    }
}
