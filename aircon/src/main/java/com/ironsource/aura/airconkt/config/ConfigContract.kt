package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

// TODO - identifiable config source won't work
// TODO - unitests
// TODO - linter

// TODO BONUS
// TODO - adapting custom configs (e.g enum to other)
// TODO - builtin constraints
// TODO - custom types - sealed class enum? (need to think about inheritors with constructor)
// TODO - revise DSL structure (+dsl annotations)

interface Defaulted<T> {
    fun default(cache: Boolean = true, provider: () -> T)
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
    var cacheValue: Boolean
}
