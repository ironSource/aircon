package com.ironsource.aura.airconkt.config

import android.content.res.Resources
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// TODO - nullable types (currently string can be used with String? property but default value cannot be null)
// TODO - Enum types, duration type
// TODO - constraints (min, max..) fallback policy
// TODO - reconsider hierarchy here (maybe simple config interface should not contain adapt method)

abstract class Config<Raw, Actual, Conf : Config<Raw, Actual, Conf>> {

    private lateinit var confKey: String
    private lateinit var sourceClass: KClass<out ConfigSource>

    lateinit var defaultValueProvider: () -> Actual

    private val constraints: MutableList<Constraint<Raw>> = mutableListOf()

    fun key(key: String): Conf {
        confKey = key
        return this as Conf
    }

    fun source(source: KClass<out ConfigSource>): Conf {
        sourceClass = source
        return this as Conf
    }

    fun defaultValue(value: Actual): Conf {
        defaultValueProvider = { value }
        return this as Conf
    }

    fun defaultValue(provider: () -> Actual): Conf {
        defaultValueProvider = provider
        return this as Conf
    }

    fun constrained(constraint: Constraint<Raw>): Conf {
        constraints.add(constraint)
        return this as Conf
    }

    fun constrained(constraint: (Raw) -> Boolean): Conf {
        constraints.add(object : Constraint<Raw> {
            override fun isValid(value: Raw): Boolean {
                return constraint(value)
            }
        })

        return this as Conf
    }

    open operator fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        return getRemoteValue(thisRef, property) { getDefaultValue() } ?: getDefaultValue()
    }

    internal fun getRemoteValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, defaultProvider: () -> Any?): Actual? {
        val source = resolveSource(thisRef)

        val key = resolveKey(property)

        val value = getRawValue(source, key)
        if (value == null) {
            log("$source - Value not found in remote, using *default* value \"$key\" -> ${defaultProvider()}")
            return null
        }

        if (!isValid(value)) {
            log("$source - Invalid value found in remote, using *default* value \"$key\" -> ${defaultProvider()}")
            return null
        }

        constraints.forEach {
            if (!it.isValid(value)) {
                log("$source - value failed to meet constraint $it, using *default* value \"$key\" -> ${defaultProvider()}")
                return null
            }
        }

        // TODO - Adapt cannot return null on failure, need to find other way represent adaption failure
        val adaptedValue = adapt(value)
        if (adaptedValue == null) {
            log("$source - Failed to adapt value found in remote, using *default* value \"$key\" -> ${defaultProvider()}")
            return null
        }

        return adaptedValue
    }

    internal abstract fun getRawValue(source: ConfigSource, key: String): Raw?

    internal abstract fun isValid(value: Raw): Boolean

    internal abstract fun adapt(value: Raw): Actual

    protected fun resolveKey(property: KProperty<*>) =
            if (::confKey.isInitialized) confKey else property.name

    protected fun resolveSource(thisRef: FeatureRemoteConfig): ConfigSource {
        val sourceClass = if (::sourceClass.isInitialized) sourceClass else thisRef.source
        return AirConKt.get()!!.configSourceRepository.getSource(sourceClass.java)
    }

    // Need linter here
    fun getDefaultValue(): Actual {
        if (!::defaultValueProvider.isInitialized) {
            throw RuntimeException("${toString()} - No default value provided!")
        }
        return defaultValueProvider()
    }

    fun hasDefaultValue() = ::defaultValueProvider.isInitialized
}

// TODO - can avoid inheritance here?
abstract class MutableConfig<Raw, Actual, Conf : MutableConfig<Raw, Actual, Conf>> : Config<Raw, Actual, Conf>() {

    abstract fun asRaw(value: Actual): Raw
    abstract fun setRawValue(source: ConfigSource, key: String, value: Raw)

    operator fun setValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, value: Actual) {
        setRawValue(resolveSource(thisRef), resolveKey(property), asRaw(value))
    }
}

abstract class SimpleConfig<Raw, Conf : SimpleConfig<Raw, Conf>> : MutableConfig<Raw, Raw, Conf>() {
    override fun isValid(value: Raw) = true

    override fun adapt(value: Raw) = value

    override fun asRaw(value: Raw) = value
}

abstract class ResourcedConfig<Raw, Actual, Conf : ResourcedConfig<Raw, Actual, Conf>> : MutableConfig<Raw, Actual, Conf>() {

    fun defaultValue(resource: Resource): Conf {
        defaultValueProvider = {
            adapt(resolve(AirConKt.get()!!.context.resources, resource))
        }
        return this as Conf
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

abstract class SimpleResourcedConfig<Raw, Conf : SimpleResourcedConfig<Raw, Conf>> : SimpleConfig<Raw, Conf>() {

    fun defaultValue(resource: Resource): Conf {
        defaultValueProvider = { resolve(AirConKt.get()!!.context.resources, resource) }
        return this as Conf
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

private fun log(msg: String) {
    AirConKt.get()
            .logger
            .v(msg)
}