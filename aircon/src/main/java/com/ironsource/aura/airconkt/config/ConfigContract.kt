package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.config.constraint.Constraint
import com.ironsource.aura.airconkt.dsl.AirConDsl
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

/**
 * TODO - FIRST RELEASE
 *
 */

/**
 * TODO - DEPLOYMENT
 * Fill documentation, README and CHANGELOG
 * Example on how to create custom config, custom constraint
 * Benchmark vs old AirCon (runtime, compile-time, sdk size, supported features..)
 */

/**
 * TODO - Missing original AirCon features
 * Solution for generic jsonConfig types.
 * Support for identifiable config source.
 * Config mocks
 * TimeConfig
 * Enum config randomizer.
 * XML injection.
 */

/**
 * TODO - Backlog
 * Think about repeatable configs
 * Custom types - sealed class enum? (need to think about inheritors with constructor)
 * Separate custom types to SDK extension module
 * Adapting custom configs (e.g enum to other)
 */

typealias SimpleConfig<T> = Config<T, T>

interface Defaulted<T> {
    fun default(cache: Boolean = true,
                provider: () -> T)

    var default: T
    var defaultRes: Int
}

interface Constrained<Test, Fallback> {
    fun constraint(name: String? = null,
                   block: Constraint<Test, Fallback?>.() -> Unit)
}

interface Processable<T> {
    fun process(processor: (T) -> T)
}

interface Adaptable<Raw, Actual> {
    fun adapt(block: Adapter<Raw, Actual>.() -> Unit)
}

interface ConfigProperty<T> : ReadWriteProperty<FeatureRemoteConfig, T>
interface ReadOnlyConfigProperty<T> : ReadOnlyProperty<FeatureRemoteConfig, T>

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