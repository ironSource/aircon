package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.Config
import com.ironsource.aura.airconkt.config.ConfigDelegate
import com.ironsource.aura.airconkt.config.SimpleConfig
import com.ironsource.aura.airconkt.config.TypeResolver

typealias IntConfig<T> = Config<Int, T>
typealias LongConfig<T> = Config<Long, T>
typealias FloatConfig<T> = Config<Float, T>
typealias StringConfig<T> = Config<String, T>
typealias BooleanConfig<T> = Config<Boolean, T>

fun <T> typedIntConfig(block: IntConfig<T>.() -> Unit): Config<Int, T> =
        ConfigDelegate(typeResolver = TypeResolver.int(),
                block = block)


fun <T> typedLongConfig(block: LongConfig<T>.() -> Unit): Config<Long, T> =
        ConfigDelegate(typeResolver = TypeResolver.long(),
                block = block)


fun <T> typedFloatConfig(block: FloatConfig<T>.() -> Unit): Config<Float, T> =
        ConfigDelegate(typeResolver = TypeResolver.float(),
                block = block)


fun <T> typedStringConfig(block: StringConfig<T>.() -> Unit): Config<String, T> =
        ConfigDelegate(typeResolver = TypeResolver.string(),
                block = block)


fun <T> typedBooleanConfig(block: BooleanConfig<T>.() -> Unit): Config<Boolean, T> =
        ConfigDelegate(typeResolver = TypeResolver.boolean(),
                block = block)

fun nullableStringConfig(block: StringConfig<String?>.() -> Unit) = typedStringConfig(block.nullableConfigBlock())

fun intConfig(block: SimpleConfig<Int>.() -> Unit) = typedIntConfig(block.simpleConfigBlock())

fun longConfig(block: SimpleConfig<Long>.() -> Unit) = typedLongConfig(block.simpleConfigBlock())

fun floatConfig(block: SimpleConfig<Float>.() -> Unit) = typedFloatConfig(block.simpleConfigBlock())

fun stringConfig(block: SimpleConfig<String>.() -> Unit) = typedStringConfig(block.simpleConfigBlock())

fun booleanConfig(block: SimpleConfig<Boolean>.() -> Unit) = typedBooleanConfig(block.simpleConfigBlock())

fun <T> (Config<T, T?>.() -> Unit).nullableConfigBlock(): Config<T, T?>.() -> Unit {
    return {
        adapt { it }
        serialize { it }
        this@nullableConfigBlock()
    }
}

fun <T> (SimpleConfig<T>.() -> Unit).simpleConfigBlock(): SimpleConfig<T>.() -> Unit {
    return {
        adapt { it }
        serialize { it }
        this@simpleConfigBlock()
    }
}