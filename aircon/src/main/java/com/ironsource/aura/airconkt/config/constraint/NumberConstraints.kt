package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.Config

var <T> Config<T, *>.minValue: T
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        minValue { this.value = value }
    }

fun <T> Config<T, *>.minValue(block: RangeConstraint<T>.() -> Unit)
        where T : Number, T : Comparable<T> {
    val rangeConstraint = RangeConstraint(block)
    rangeFallback("min value", rangeConstraint) { it >= rangeConstraint.value!! }
}

var <T> Config<T, *>.maxValue: T
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        maxValue { this.value = value }
    }

fun <T> Config<T, *>.maxValue(block: RangeConstraint<T>.() -> Unit)
        where T : Number, T : Comparable<T> {
    val rangeConstraint = RangeConstraint(block)
    rangeFallback("max value", rangeConstraint) { it <= rangeConstraint.value!! }
}

private fun <T, S> Config<T, S>.rangeFallback(name: String,
                                              rangeConstraint: RangeConstraint<T>,
                                              allowBlock: (T) -> Boolean)
        where T : Number, T : Comparable<T> {
    constraint(name) {
        acceptIf(allowBlock)
        if (rangeConstraint.fallbackPolicy == FallbackPolicy.RANGE) {
            fallbackToPrimitive = rangeConstraint.value!!
        }
    }
}

enum class FallbackPolicy {
    DEFAULT,
    RANGE
}

class RangeConstraint<T> private constructor() {

    var value: T? = null
    var fallbackPolicy = FallbackPolicy.DEFAULT

    companion object {
        internal operator fun <T> invoke(
                block: RangeConstraint<T>.() -> Unit) = RangeConstraint<T>().apply(
                block)
    }
}