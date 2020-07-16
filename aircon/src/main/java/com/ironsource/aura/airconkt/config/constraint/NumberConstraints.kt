package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.ReadOnlyConfig

var <T> ReadOnlyConfig<T, *>.minValue: T
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        minValue(value)
    }

// TODO - not exactly DSLi
fun <T> ReadOnlyConfig<T, *>.minValue(value: T,
                                      fallbackPolicy: FallbackPolicy = FallbackPolicy.DEFAULT)
        where T : Number, T : Comparable<T> {
    rangeFallback(fallbackPolicy, value) { it >= value }
}

var <T> ReadOnlyConfig<T, *>.maxValue: T
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        maxValue(value)
    }

fun <T> ReadOnlyConfig<T, *>.maxValue(value: T,
                                      fallbackPolicy: FallbackPolicy = FallbackPolicy.DEFAULT)
        where T : Number, T : Comparable<T> {
    rangeFallback(fallbackPolicy, value) { it <= value }
}

private fun <T, S> ReadOnlyConfig<T, S>.rangeFallback(fallbackPolicy: FallbackPolicy,
                                                      value: T, allowBlock: (T) -> Boolean)
        where T : Number, T : Comparable<T> {
    constraint {
        acceptIf(allowBlock)
        if (fallbackPolicy == FallbackPolicy.RANGE) {
            fallbackToPrimitive = value
        }
    }
}

enum class FallbackPolicy {
    DEFAULT,
    RANGE
}