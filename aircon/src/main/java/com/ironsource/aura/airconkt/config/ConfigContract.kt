package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.dsl.AirConDsl
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

// TODO - formatter
// TODO - unitests (custom configs)
// TODO - linter
// TODO - missing documentation, README and CHANGELOG
// TODO - benchmark vs old AirCon (runtime, compile-time, supported features..)

// TODO BONUS
// TODO - reintroduce support for identifiable config source
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
    fun process(processor: (T) -> T)
}

interface Adaptable<Raw, Actual> {
    fun adapt(adapter: (Raw) -> Actual?)
    fun serialize(serializer: (Actual) -> Raw?)
}

@AirConDsl
interface Config<Raw, Actual> :
        ReadWriteProperty<FeatureRemoteConfig, Actual>,
        Defaulted<Actual>,
        Constrained<Raw, Actual>,
        Processable<Actual> {
    var key: String
    var source: KClass<out ConfigSource>
    var cacheValue: Boolean
}

@AirConDsl
interface AdaptableConfig<Raw, Actual> :
        Config<Raw, Actual>,
        Adaptable<Raw, Actual>