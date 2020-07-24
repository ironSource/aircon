package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.AdaptableConfig
import com.ironsource.aura.airconkt.config.ConfigPropertyFactory
import com.ironsource.aura.airconkt.config.SimpleConfig
import com.ironsource.aura.airconkt.config.SourceTypeResolver

fun <T> typedIntConfig(block: AdaptableConfig<Int, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.int(), block = block)

fun <T> typedLongConfig(block: AdaptableConfig<Long, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.long(), block = block)

fun <T> typedFloatConfig(block: AdaptableConfig<Float, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.float(), block = block)

fun <T> typedStringConfig(block: AdaptableConfig<String, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.string(), block = block)

fun <T> typedBooleanConfig(block: AdaptableConfig<Boolean, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.boolean(), block = block)

fun nullableStringConfig(block: AdaptableConfig<String, String?>.() -> Unit) =
        ConfigPropertyFactory.fromNullablePrimitive(SourceTypeResolver.string(), block = block)

fun intConfig(block: SimpleConfig<Int>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.int(), block = block)

fun longConfig(block: SimpleConfig<Long>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.long(), block = block)

fun floatConfig(block: SimpleConfig<Float>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.float(), block = block)

fun stringConfig(block: SimpleConfig<String>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.string(), block = block)

fun booleanConfig(block: SimpleConfig<Boolean>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.boolean(), block = block)

inline fun <reified T> typedConfig(noinline block: AdaptableConfig<Any, T>.() -> Unit) =
        ConfigPropertyFactory.from<Any, T>(SourceTypeResolver.any(),
                getterAdapter = { it as? T },
                setterAdapter = { it },
                block = block)
