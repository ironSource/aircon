package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.config.constraint.Constraint
import com.ironsource.aura.airconkt.dsl.AirConDsl
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

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

@AirConDsl
interface Config<Raw, Actual> :
        Defaulted<Actual>,
        Constrained<Raw, Actual>,
        Processable<Actual> {

    var key: String
    var source: KClass<out ConfigSource>
    var cached: Boolean
}

@AirConDsl
interface AdaptableConfig<Raw, Actual> :
        Config<Raw, Actual>,
        Adaptable<Raw, Actual>

interface ConfigProperty<T> : ReadWriteProperty<FeatureRemoteConfig, T>
interface ReadOnlyConfigProperty<T> : ReadOnlyProperty<FeatureRemoteConfig, T>