package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.AdaptableConfig
import com.ironsource.aura.airconkt.config.ConfigDelegate
import com.ironsource.aura.airconkt.config.SimpleConfig
import com.ironsource.aura.airconkt.config.SourceTypeResolver

typealias IntConfig<T> = AdaptableConfig<Int, T>
typealias LongConfig<T> = AdaptableConfig<Long, T>
typealias FloatConfig<T> = AdaptableConfig<Float, T>
typealias StringConfig<T> = AdaptableConfig<String, T>
typealias BooleanConfig<T> = AdaptableConfig<Boolean, T>
typealias AnyConfig<T> = AdaptableConfig<Any, T>

fun <T> typedIntConfig(block: IntConfig<T>.() -> Unit): AdaptableConfig<Int, T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.int(),
                block = block)


fun <T> typedLongConfig(block: LongConfig<T>.() -> Unit): AdaptableConfig<Long, T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.long(),
                block = block)


fun <T> typedFloatConfig(block: FloatConfig<T>.() -> Unit): AdaptableConfig<Float, T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.float(),
                block = block)


fun <T> typedStringConfig(block: StringConfig<T>.() -> Unit): AdaptableConfig<String, T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.string(),
                block = block)


fun <T> typedBooleanConfig(block: BooleanConfig<T>.() -> Unit): AdaptableConfig<Boolean, T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.boolean(),
                block = block)

fun nullableStringConfig(block: StringConfig<String?>.() -> Unit) = typedStringConfig(block.nullableConfigBlock())

fun intConfig(block: SimpleConfig<Int>.() -> Unit) = typedIntConfig(block.simpleConfigBlock())

fun longConfig(block: SimpleConfig<Long>.() -> Unit) = typedLongConfig(block.simpleConfigBlock())

fun floatConfig(block: SimpleConfig<Float>.() -> Unit) = typedFloatConfig(block.simpleConfigBlock())

fun stringConfig(block: SimpleConfig<String>.() -> Unit) = typedStringConfig(block.simpleConfigBlock())

fun booleanConfig(block: SimpleConfig<Boolean>.() -> Unit) = typedBooleanConfig(block.simpleConfigBlock())

// Experimental feature
inline fun <reified T> typedConfig(crossinline block: AnyConfig<T>.() -> Unit): AdaptableConfig<Any, T> =
        ConfigDelegate(sourceTypeResolver = SourceTypeResolver.any(),
                block = {
                    adapt { it as? T }
                    serialize { it }
                    block()
                })

private fun <T> (AdaptableConfig<T, T?>.() -> Unit).nullableConfigBlock(): AdaptableConfig<T, T?>.() -> Unit {
    return {
        adapt { it }
        serialize { it }
        this@nullableConfigBlock()
    }
}

private fun <T> (SimpleConfig<T>.() -> Unit).simpleConfigBlock(): SimpleConfig<T>.() -> Unit {
    return {
        adapt { it }
        serialize { it }
        this@simpleConfigBlock()
    }
}