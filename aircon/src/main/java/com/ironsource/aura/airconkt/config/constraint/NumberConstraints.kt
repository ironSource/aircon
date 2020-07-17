package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.Config

val DEFAULT_POLICY = FallbackPolicy.DEFAULT

var <T> Config<T, *>.minValue: T
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        minValue(value)
    }

// TODO - not exactly DSLi
fun <T> Config<T, *>.minValue(value: T,
                              fallbackPolicy: FallbackPolicy = DEFAULT_POLICY)
        where T : Number, T : Comparable<T> {
    rangeFallback("min value", fallbackPolicy, value) { it >= value }
}

var <T> Config<T, *>.maxValue: T
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        maxValue(value)
    }

fun <T> Config<T, *>.maxValue(value: T,
                              fallbackPolicy: FallbackPolicy = DEFAULT_POLICY)
        where T : Number, T : Comparable<T> {
    rangeFallback("max value", fallbackPolicy, value) { it <= value }
}

private fun <T, S> Config<T, S>.rangeFallback(name: String, fallbackPolicy: FallbackPolicy,
                                              value: T, allowBlock: (T) -> Boolean)
        where T : Number, T : Comparable<T> {
    constraint(name) {
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