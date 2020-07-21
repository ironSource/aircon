package com.ironsource.aura.airconkt.utils

internal fun <T> (() -> T).toCached(): () -> T {
    var result: T? = null
    return {
        if (result != null) {
            result!!
        } else {
            val blockValue = this()
            result = blockValue
            blockValue
        }
    }
}

internal fun <T, S> ((T) -> S).toCached(): (T) -> S {
    var result: S? = null
    return {
        if (result != null) {
            result!!
        } else {
            val blockValue = this(it)
            result = blockValue
            blockValue
        }
    }
}