package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.ConfigDelegate
import com.ironsource.aura.airconkt.config.TypeResolver
import kotlin.reflect.KClass

fun <T : Enum<T>> intEnumConfig(enumClass: KClass<T>, block: IntConfig<T>.() -> Unit) =
        ConfigDelegate(TypeResolver.int(),
                adapter = { getIntEnumConst(enumClass, it) },
                serializer = {
                    val remoteValue = getIntEnumRemoteValue(enumClass, it)
                    remoteValue
                            ?: throw IllegalArgumentException("No remote value annotation defined for $enumClass.$it")
                },
                block = block
        )

fun <T : Enum<T>> stringEnumConfig(enumClass: KClass<T>, block: StringConfig<T>.() -> Unit) =
        ConfigDelegate(TypeResolver.string(),
                adapter = { getStringEnumConst(enumClass, it) },
                serializer = {
                    val remoteValue = getStringEnumRemoteValue(enumClass, it)
                    remoteValue
                            ?: throw IllegalArgumentException("No remote value annotation defined for $enumClass.$it")
                },
                block = block
        )