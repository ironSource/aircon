package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.*

typealias AdaptableIntConfig<T> = AdaptableConfig<Int, T>
typealias AdaptableLongConfig<T> = AdaptableConfig<Long, T>
typealias AdaptableFloatConfig<T> = AdaptableConfig<Float, T>
typealias AdaptableStringConfig<T> = AdaptableConfig<String, T>
typealias AdaptableBooleanConfig<T> = AdaptableConfig<Boolean, T>
typealias AdaptableAnyConfig<T> = AdaptableConfig<Any, T>
typealias PrimitiveConfig<T> = Config<T, T>

fun <T> typedIntConfig(block: AdaptableIntConfig<T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.int(),
                block = block)

fun <T> typedLongConfig(block: AdaptableLongConfig<T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.long(),
                block = block)


fun <T> typedFloatConfig(block: AdaptableFloatConfig<T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.float(),
                block = block)


fun <T> typedStringConfig(block: AdaptableStringConfig<T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.string(),
                block = block)


fun <T> typedBooleanConfig(block: AdaptableBooleanConfig<T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.boolean(),
                block = block)

fun nullableStringConfig(block: AdaptableStringConfig<String?>.() -> Unit): ConfigProperty<String?> =
        typedStringConfig(block.nullableConfigBlock())

fun intConfig(block: PrimitiveConfig<Int>.() -> Unit): ConfigProperty<Int> =
        typedIntConfig(block.primitiveConfigBlock())

fun longConfig(block: PrimitiveConfig<Long>.() -> Unit): ConfigProperty<Long> =
        typedLongConfig(block.primitiveConfigBlock())

fun floatConfig(block: PrimitiveConfig<Float>.() -> Unit): ConfigProperty<Float> =
        typedFloatConfig(block.primitiveConfigBlock())

fun stringConfig(block: PrimitiveConfig<String>.() -> Unit): ConfigProperty<String> =
        typedStringConfig(block.primitiveConfigBlock())

fun booleanConfig(block: PrimitiveConfig<Boolean>.() -> Unit): ConfigProperty<Boolean> =
        typedBooleanConfig(block.primitiveConfigBlock())

// Experimental feature
inline fun <reified T> typedConfig(crossinline block: AdaptableAnyConfig<T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.any(),
                block = {
                    adapt { it as? T }
                    serialize { it }
                    block()
                })

private fun <T> (PrimitiveConfig<T>.() -> Unit).primitiveConfigBlock(): AdaptableConfig<T, T>.() -> Unit {
    return {
        adapt { it }
        serialize { it }
        this@primitiveConfigBlock()
    }
}

private fun <T> (AdaptableConfig<T, T?>.() -> Unit).nullableConfigBlock(): AdaptableConfig<T, T?>.() -> Unit {
    return {
        adapt { it }
        serialize { it }
        this@nullableConfigBlock()
    }
}
