package com.ironsource.aura.airconkt.config

fun intConfig(block: ReadWriteConfig<Int, Int>.() -> Unit) = createConfig(block) {
    createConfig(block) {
        PrimitiveConfigDelegate(ConfigSourceResolver.Int, ResourcesResolver.Int)
    }
}

fun longConfig(block: ReadWriteConfig<Long, Long>.() -> Unit) = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.Long, ResourcesResolver.Long)
}

fun floatConfig(block: ReadWriteConfig<Float, Float>.() -> Unit) = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.Float, ResourcesResolver.Float)
}

fun stringConfig(block: ReadWriteConfig<String, String>.() -> Unit) =
        PrimitiveConfigDelegate(ConfigSourceResolver.String, ResourcesResolver.String)

fun booleanConfig(block: ReadWriteConfig<Boolean, Boolean>.() -> Unit) = createConfig(block) {
    PrimitiveConfigDelegate(ConfigSourceResolver.Boolean, ResourcesResolver.Boolean)
}
