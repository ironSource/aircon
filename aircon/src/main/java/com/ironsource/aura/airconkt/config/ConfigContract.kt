package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.dsl.AirConDsl
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

// TODO - FIRST RELEASE
// ----------
// TODO - adapting custom configs (e.g enum to other)
// ----------

// TODO - DEPLOYMENT
// ----------
// TODO - fill documentation, README and CHANGELOG
// TODO - benchmark vs old AirCon (runtime, compile-time, supported features..)
// ----------

// TODO - Backlog
// ----------
// TODO - solution for generic jsonConfig types
// TODO - reintroduce support for identifiable config source
// TODO - custom types - sealed class enum? (need to think about inheritors with constructor)
// ----------

typealias SimpleConfig<T> = Config<T, T>

interface Defaulted<T> {
    fun default(cache: Boolean = true,
                provider: () -> T)

    var default: T
    var defaultRes: Int
}

interface Constrained<Test, Fallback> {
    fun constraint(name: String? = null,
                   block: ConstraintBuilder<Test, Fallback?>.() -> Unit)
}

interface Processable<T> {
    fun process(processor: (T) -> T)
}

interface Adaptable<Raw, Actual> {
    fun adapt(adapter: (Raw) -> Actual?)
    fun serialize(serializer: (Actual) -> Raw?)
}

interface ConfigProperty<T> : ReadWriteProperty<FeatureRemoteConfig, T>

@AirConDsl
interface Config<Raw, Actual> :
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