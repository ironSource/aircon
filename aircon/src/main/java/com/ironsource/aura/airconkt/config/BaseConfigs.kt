package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.Constraint
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// TODO - revive adapted
// TODO - nullable types (currently string can be used with String? property but default value cannot be null)
// TODO - Enum types, duration type
// TODO - constraints (min, max..) fallback policy

interface Defaulted<T> {
    var default: T
    var defaultRes: Int
    fun defaultProvider(provider: () -> T)
}

interface Constrained<T> {
    fun constraint(constraint: Constraint<T>)
    fun constraint(constraint: (T) -> Boolean)
}

interface Processable<T> {
    var processor: ((T) -> T)
}

interface ReadOnlyConfig<Raw, Actual> :
        ReadOnlyProperty<FeatureRemoteConfig, Actual>,
        Defaulted<Actual>,
        Constrained<Raw>,
        Processable<Actual> {
    var key: String
    var source: KClass<out ConfigSource>
}

open class ReadOnlyConfigDelegate<Raw, Actual> internal constructor(
        protected val configSourceResolver: ConfigSourceResolver<Raw>,
        protected val resourcesResolver: ResourcesResolver<Raw>,
        protected val validator: (Raw) -> Boolean,
        protected val adapter: (Raw) -> Actual
) : ReadOnlyConfig<Raw, Actual> {

    override lateinit var key: String
    override lateinit var source: KClass<out ConfigSource>
    override lateinit var processor: ((Actual) -> Actual)

    override var default: Actual
        get() = defaultValueProvider()
        set(value) {
            defaultValueProvider = { value }
        }

    override var defaultRes: Int
        get() = 0
        set(value) {
            defaultValueProvider = {
                val adapted = adapter(resourcesResolver.resourcesGetter(AirConKt.get()!!.context.resources, value))
                adapted ?: throw RuntimeException("Failed to adapt default resource value to type")
            }
        }

    internal lateinit var defaultValueProvider: () -> Actual

    private val constraints: MutableList<Constraint<Raw>> = mutableListOf()

    override fun defaultProvider(provider: () -> Actual) {
        defaultValueProvider = provider
    }

    override fun constraint(constraint: Constraint<Raw>) {
        constraints.add(constraint)
    }

    override fun constraint(constraint: (Raw) -> Boolean) {
        constraints.add(object : Constraint<Raw> {
            override fun isValid(value: Raw): Boolean {
                return constraint(value)
            }
        })
    }

    override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        return getRemoteValue(thisRef, property) { default } ?: default
    }

    internal fun getRemoteValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, defaultProvider: () -> Any?): Actual? {
        val key = resolveKey(property)
        val source = resolveSource(thisRef)

        val value = configSourceResolver.sourceGetter(source, key)
        if (value == null) {
            log("$source - Value not found in remote, using *default* value \"$key\" -> ${defaultValueProvider()}")
            return null
        }

        if (!validate(value)) {
            return null
        }

        val adaptedValue = process(value)
        if (adaptedValue == null) {
            return null
        }

        return adaptedValue
    }

    private fun validate(value: Raw): Boolean {
        if (!validator(value)) {
            log("$source - Invalid value found in remote, using *default* value \"$key\" -> ${defaultValueProvider()}")
            return false;
        }

        constraints.forEach {
            if (!it.isValid(value)) {
                log("$source - value failed to meet constraint $it, using *default* value \"$key\" -> ${defaultValueProvider()}")
                return false
            }
        }

        return true
    }

    private fun process(value: Raw): Actual? {
        var adaptedValue = adapter(value)
        if (adaptedValue == null) {
            log("$source - Failed to adapt value found in remote, using *default* value \"$key\" -> ${defaultValueProvider()}")
            return null
        }

        if (::processor.isInitialized) {
            adaptedValue = processor(adaptedValue)
        }

        return adaptedValue
    }

    internal fun resolveKey(property: KProperty<*>) =
            if (::key.isInitialized) key else property.name

    internal fun resolveSource(thisRef: FeatureRemoteConfig): ConfigSource {
        val sourceClass = if (::source.isInitialized) source else thisRef.source
        return AirConKt.get()!!.configSourceRepository.getSource(sourceClass.java)
    }

    internal fun hasDefaultValue() = ::defaultValueProvider.isInitialized
}


interface ReadWriteConfig<Raw, Actual> :
        ReadWriteProperty<FeatureRemoteConfig, Actual>,
        ReadOnlyConfig<Raw, Actual>

open class ReadWriteConfigDelegate<Raw, Actual>(
        configSourceResolver: ConfigSourceResolver<Raw>,
        resourcesResolver: ResourcesResolver<Raw>,
        validator: (Raw) -> Boolean,
        adapter: (Raw) -> Actual,
        private val serializer: (Actual) -> Raw
) : ReadOnlyConfigDelegate<Raw, Actual>(configSourceResolver, resourcesResolver, validator, adapter),
        ReadWriteConfig<Raw, Actual> {

    override fun setValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, value: Actual) {
        val source = resolveSource(thisRef)
        val key = resolveKey(property)

        configSourceResolver.sourceSetter(source, key, serializer(value))
    }
}

class PrimitiveConfigDelegate<T>(configSourceResolver: ConfigSourceResolver<T>,
                                 resourcesResolver: ResourcesResolver<T>,
                                 validator: (T) -> Boolean = { true },
                                 adapter: (T) -> T = { it },
                                 serializer: (T) -> T = { it })
    : ReadWriteConfigDelegate<T, T>(configSourceResolver, resourcesResolver,
        validator, adapter, serializer)

fun <Raw, Actual, Conf : ReadOnlyConfig<Raw, Actual>> createConfig(
        block: Conf.() -> Unit,
        create: () -> Conf): Conf {
    val config = create()
    config.block()
    return config
}

private fun log(msg: String) {
    AirConKt.get()
            .logger
            .v(msg)
}