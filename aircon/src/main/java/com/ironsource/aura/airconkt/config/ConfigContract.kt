package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

// TODO - adapting custom configs (e.g enum to other)
// TODO - sealed class enum? (need to think about inheritors with constructor)
// TODO - caching option (defaultValue, final value...)
// TODO - proguard (R8) rules - remote config properties names should not be touched
// TODO - aux methods (isConfigured, getRawValue, getDefaultValue)...

// TODO ONGOING - builtin constraints (e.g acceptedValues)

interface Defaulted<T> {
    fun default(provider: () -> T)
    var default: T
    var defaultRes: Int
}

interface Constrained<Test, Fallback> {
    fun constraint(name: String? = null, block: ConstraintBuilder<Test, Fallback?>.() -> Unit)
}

interface Processable<T> {
    fun process(processor: ((T) -> T))
}

interface Adaptable<Raw, Actual> {
    fun adapt(adapter: (Raw) -> Actual?)
    fun serialize(serializer: (Actual) -> Raw?)
}

interface Config<Raw, Actual> :
        ReadWriteProperty<FeatureRemoteConfig, Actual>,
        Defaulted<Actual>,
        Constrained<Raw, Actual>,
        Processable<Actual>,
        Adaptable<Raw, Actual> {
    var key: String
    var source: KClass<out ConfigSource>
}

fun <Raw, Actual, Conf : Config<Raw, Actual>> createConfig(
        block: Conf.() -> Unit,
        create: () -> Conf) = create().apply(block)
