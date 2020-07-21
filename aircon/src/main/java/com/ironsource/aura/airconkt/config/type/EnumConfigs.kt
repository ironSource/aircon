package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.Config
import com.ironsource.aura.airconkt.config.ConfigProperty
import com.ironsource.aura.airconkt.config.ConfigPropertyFactory
import com.ironsource.aura.airconkt.config.SourceTypeResolver

inline fun <reified T : Enum<T>> intEnumConfig(noinline block: Config<Int, T> .() -> Unit): ConfigProperty<T> {
    return ConfigPropertyFactory.from(SourceTypeResolver.int(),
            adapter = { Utils.getIntEnumConst(T::class, it) },
            serializer = {
                val remoteValue = Utils.getIntEnumRemoteValue(T::class, it)
                remoteValue
                        ?: throw IllegalArgumentException("No remote value annotation defined for ${T::class}.$it")
            },
            block = block
    )
}

inline fun <reified T : Enum<T>> stringEnumConfig(noinline block: Config<String, T> .() -> Unit): ConfigProperty<T> {
    return ConfigPropertyFactory.from(SourceTypeResolver.string(),
            adapter = { Utils.getStringEnumConst(T::class, it) },
            serializer = {
                val remoteValue = Utils.getStringEnumRemoteValue(T::class, it)
                remoteValue
                        ?: throw IllegalArgumentException("No remote value annotation defined for ${T::class}.$it")
            },
            block = block
    )
}