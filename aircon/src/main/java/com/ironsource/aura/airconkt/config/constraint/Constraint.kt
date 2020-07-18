package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.utils.toCached

class ConstraintBuilder<Test, Fallback> private constructor(var name: String? = null,
                                                            private var adapter: (Test) -> Fallback) {
    internal var verifiers: MutableList<(Test) -> Boolean> = mutableListOf()
    internal var fallbackProvider: ((Test) -> Fallback)? = null

    companion object {
        internal operator fun <T, S> invoke(name: String? = null,
                                            adapter: (T) -> S,
                                            block: ConstraintBuilder<T, S>.() -> Unit) =
                ConstraintBuilder(name, adapter).apply(block)
    }

    fun acceptIf(block: (Test) -> Boolean) {
        verifiers.add(block)
    }

    fun denyIf(block: (Test) -> Boolean) {
        verifiers.add { !block(it) }
    }

    var fallbackToPrimitive: Test
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            fallbackToPrimitive { value }
        }

    fun fallbackToPrimitive(fallbackProvider: (Test) -> Test) {
        fallbackTo { adapter(fallbackProvider(it)) }
    }

    var fallbackTo: Fallback
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            fallbackTo { value }
        }

    fun fallbackTo(cache: Boolean = true, fallbackProvider: (Test) -> Fallback) {
        this.fallbackProvider = if (cache) fallbackProvider.toCached() else fallbackProvider
    }
}