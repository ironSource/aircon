package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.*

class PrimitiveConfigDelegate<T>(configSourceResolver: ConfigSourceResolver<T>,
                                 resourcesResolver: ResourcesResolver<T>,
                                 validator: (T) -> Boolean = { true },
                                 adapter: (T) -> T = { it },
                                 serializer: (T) -> T = { it })
    : ReadWriteConfigDelegate<T, T>(configSourceResolver, resourcesResolver,
        validator, adapter, serializer)

fun intConfig(block: ReadWriteConfig<Int, Int>.() -> Unit): ReadWriteConfig<Int, Int> = createConfig(block) {
    createConfig(block) {
        PrimitiveConfigDelegate(ConfigSourceResolver.Int, ResourcesResolver.Int)
    }
}

fun longConfig(block: ReadWriteConfig<Long, Long>.() -> Unit): ReadWriteConfig<Long, Long> = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.Long, ResourcesResolver.Long)
}

fun floatConfig(block: ReadWriteConfig<Float, Float>.() -> Unit): ReadWriteConfig<Float, Float> = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.Float, ResourcesResolver.Float)
}

fun stringConfig(block: ReadWriteConfig<String, String>.() -> Unit): ReadWriteConfig<String, String> = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.String, ResourcesResolver.String)
}

fun booleanConfig(block: ReadWriteConfig<Boolean, Boolean>.() -> Unit): ReadWriteConfig<Boolean, Boolean> = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.Boolean, ResourcesResolver.Boolean)
}
