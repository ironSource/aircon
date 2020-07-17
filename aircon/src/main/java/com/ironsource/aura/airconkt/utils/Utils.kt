package com.ironsource.aura.airconkt.utils

fun <T> cachedBlock(block: () -> T): () -> T {
    var result: T? = null
    return {
        if (result != null) {
            result!!
        } else {
            val blockValue = block()
            result = blockValue
            blockValue
        }
    }
}

fun <T, S> cachedBlock(block: (T) -> S): (T) -> S {
    var result: S? = null
    return {
        if (result != null) {
            result!!
        } else {
            val blockValue = block(it)
            result = blockValue
            blockValue
        }
    }
}