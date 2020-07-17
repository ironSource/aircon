package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.ReadOnlyConfig

var <T> ReadOnlyConfig<T, *>.whiteList: Iterable<T>
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        generalConstraint("whitelist") { it in value }
    }

var <T> ReadOnlyConfig<T, *>.blackList: Iterable<T>
        where T : Number, T : Comparable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        generalConstraint("blacklist") { it !in value }
    }


private fun <T, S> ReadOnlyConfig<T, S>.generalConstraint(name: String, allowBlock: (T) -> Boolean)
        where T : Number, T : Comparable<T> {
    constraint(name) {
        acceptIf(allowBlock)
    }
}