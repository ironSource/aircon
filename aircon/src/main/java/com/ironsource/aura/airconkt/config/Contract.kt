package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

interface Defaulted<T> {
    fun default(provider: () -> T)
    var default: T
    var defaultRes: Int
}

interface Constrained<Test, Fallback> {
    fun constraint(name: String? = null, block: ConstraintBuilder<Test, Fallback?>.() -> Unit)
}

interface Processable<T> {
    var processor: ((T) -> T)
}

interface ReadOnlyConfig<Raw, Actual> :
        ReadOnlyProperty<FeatureRemoteConfig, Actual>,
        Defaulted<Actual>,
        Constrained<Raw, Actual>,
        Processable<Actual> {
    var key: String
    var source: KClass<out ConfigSource>
}

interface ReadWriteConfig<Raw, Actual> :
        ReadWriteProperty<FeatureRemoteConfig, Actual>,
        ReadOnlyConfig<Raw, Actual>


fun <Raw, Actual, Conf : ReadOnlyConfig<Raw, Actual>> createConfig(
        block: Conf.() -> Unit,
        create: () -> Conf) = create().apply(block)
