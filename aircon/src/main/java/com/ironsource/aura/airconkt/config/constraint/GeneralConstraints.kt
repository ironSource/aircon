package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.ReadOnlyConfig

var <T> ReadOnlyConfig<T, *>.whitelist: Iterable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        generalConstraint("whitelist") { it in value }
    }

var <T> ReadOnlyConfig<T, *>.blacklist: Iterable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        generalConstraint("blacklist") { it !in value }
    }


private fun <T, S> ReadOnlyConfig<T, S>.generalConstraint(name: String, allowBlock: (T) -> Boolean) {
    constraint(name) {
        acceptIf(allowBlock)
    }
}