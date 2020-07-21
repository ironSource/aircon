package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.AdaptableConfig
import com.ironsource.aura.airconkt.config.ConfigPropertyFactory
import com.ironsource.aura.airconkt.config.SimpleConfig
import com.ironsource.aura.airconkt.config.SourceTypeResolver

fun <T> typedIntConfig(block: AdaptableConfig<Int, T>.() -> Unit) =
        ConfigPropertyFactory.from(sourceTypeResolver = SourceTypeResolver.int(),
                block = block)

fun <T> typedLongConfig(block: AdaptableConfig<Long, T>.() -> Unit) =
        ConfigPropertyFactory.from(sourceTypeResolver = SourceTypeResolver.long(),
                block = block)

fun <T> typedFloatConfig(block: AdaptableConfig<Float, T>.() -> Unit) =
        ConfigPropertyFactory.from(sourceTypeResolver = SourceTypeResolver.float(),
                block = block)

fun <T> typedStringConfig(block: AdaptableConfig<String, T>.() -> Unit) =
        ConfigPropertyFactory.from(sourceTypeResolver = SourceTypeResolver.string(),
                block = block)

fun <T> typedBooleanConfig(block: AdaptableConfig<Boolean, T>.() -> Unit) =
        ConfigPropertyFactory.from(sourceTypeResolver = SourceTypeResolver.boolean(),
                block = block)

fun nullableStringConfig(block: AdaptableConfig<String, String?>.() -> Unit) =
        typedStringConfig(block.nullableConfigBlock())

fun intConfig(block: SimpleConfig<Int>.() -> Unit) =
        typedIntConfig(block.primitiveConfigBlock())

fun longConfig(block: SimpleConfig<Long>.() -> Unit) =
        typedLongConfig(block.primitiveConfigBlock())

fun floatConfig(block: SimpleConfig<Float>.() -> Unit) =
        typedFloatConfig(block.primitiveConfigBlock())

fun stringConfig(block: SimpleConfig<String>.() -> Unit) =
        typedStringConfig(block.primitiveConfigBlock())

fun booleanConfig(block: SimpleConfig<Boolean>.() -> Unit) =
        typedBooleanConfig(block.primitiveConfigBlock())

// Experimental feature
inline fun <reified T> typedConfig(crossinline block: AdaptableConfig<Any, T>.() -> Unit) =
        ConfigPropertyFactory.from<Any, T>(sourceTypeResolver = SourceTypeResolver.any(),
                block = {
                    adapt { it as? T }
                    serialize { it }
                    block()
                })

private fun <T> (SimpleConfig<T>.() -> Unit).primitiveConfigBlock(): AdaptableConfig<T, T>.() -> Unit {
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
