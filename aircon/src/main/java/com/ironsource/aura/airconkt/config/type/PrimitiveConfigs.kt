@file:Suppress("unused")

package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.*

fun <T> FeatureRemoteConfig.typedIntConfig(block: AdaptableConfig<Int, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.int(), block = block)

fun <T> FeatureRemoteConfig.typedLongConfig(block: AdaptableConfig<Long, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.long(), block = block)

fun <T> FeatureRemoteConfig.typedFloatConfig(block: AdaptableConfig<Float, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.float(), block = block)

fun <T> FeatureRemoteConfig.typedStringConfig(block: AdaptableConfig<String, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.string(), block = block)

fun <T> FeatureRemoteConfig.typedStringSetConfig(block: AdaptableConfig<Set<String>, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.stringSet(), block = block)

fun <T> FeatureRemoteConfig.typedBooleanConfig(block: AdaptableConfig<Boolean, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.boolean(), block = block)

fun FeatureRemoteConfig.nullableStringConfig(block: AdaptableConfig<String, String?>.() -> Unit) =
        ConfigPropertyFactory.fromNullablePrimitive(SourceTypeResolver.string(), block = block)

fun FeatureRemoteConfig.nullableStringSetConfig(block: AdaptableConfig<Set<String>, Set<String>?>.() -> Unit) =
        ConfigPropertyFactory.fromNullablePrimitive(SourceTypeResolver.stringSet(), block = block)

fun FeatureRemoteConfig.intConfig(block: SimpleConfig<Int>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.int(), block = block)

fun FeatureRemoteConfig.longConfig(block: SimpleConfig<Long>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.long(), block = block)

fun FeatureRemoteConfig.floatConfig(block: SimpleConfig<Float>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.float(), block = block)

fun FeatureRemoteConfig.stringConfig(block: SimpleConfig<String>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.string(), block = block)

fun FeatureRemoteConfig.stringSetConfig(block: SimpleConfig<Set<String>>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.stringSet(), block = block)

fun FeatureRemoteConfig.booleanConfig(block: SimpleConfig<Boolean>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.boolean(), block = block)

inline fun <reified T> FeatureRemoteConfig.typedConfig(noinline block: AdaptableConfig<Any, T>.() -> Unit) =
        ConfigPropertyFactory.from<Any, T>(SourceTypeResolver.any(),
                getterAdapter = { it as? T },
                setterAdapter = { it },
                block = block)
