package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.Config
import com.ironsource.aura.airconkt.config.ConfigProperty
import com.ironsource.aura.airconkt.config.ConfigPropertyFactory
import com.ironsource.aura.airconkt.config.SourceTypeResolver
import com.ironsource.aura.airconkt.config.type.util.RemoteValueEnumUtils

inline fun <reified T : Enum<T>> intEnumConfig(noinline block: Config<Int, T> .() -> Unit): ConfigProperty<T> {
    return ConfigPropertyFactory.from(SourceTypeResolver.int(),
            adapter = { RemoteValueEnumUtils.getIntEnumConst(T::class, it) },
            serializer = {
                val remoteValue = RemoteValueEnumUtils.getIntEnumRemoteValue(T::class, it)
                remoteValue
                        ?: throw IllegalArgumentException("No remote value annotation defined for ${T::class}.$it")
            },
            block = block
    )
}

inline fun <reified T : Enum<T>> stringEnumConfig(noinline block: Config<String, T> .() -> Unit): ConfigProperty<T> {
    return ConfigPropertyFactory.from(SourceTypeResolver.string(),
            adapter = { RemoteValueEnumUtils.getStringEnumConst(T::class, it) },
            serializer = {
                val remoteValue = RemoteValueEnumUtils.getStringEnumRemoteValue(T::class, it)
                remoteValue
                        ?: throw IllegalArgumentException("No remote value annotation defined for ${T::class}.$it")
            },
            block = block
    )
}