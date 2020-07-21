package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.AdaptableConfig

var <T> AdaptableConfig<T, *>.whitelist: Iterable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        generalConstraint("whitelist") { it in value }
    }

var <T> AdaptableConfig<T, *>.blacklist: Iterable<T>
    @Deprecated("", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException()
    set(value) {
        generalConstraint("blacklist") { it !in value }
    }


private fun <T, S> AdaptableConfig<T, S>.generalConstraint(name: String, allowBlock: (T) -> Boolean) {
    constraint(name) {
        acceptIf(allowBlock)
    }
}