package com.ironsource.aura.airconkt.config

import android.content.res.Resources
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// TODO - nullable types
// TODO - Enum types, json type, duration type
// TODO - constraints (min, max..)
interface Config<Raw, Actual> : ReadOnlyProperty<FeatureRemoteConfig, Actual> {
    fun getDefault(): Actual
    fun getRawValue(source: ConfigSource, key: String): Raw?
    fun isValid(value: Raw): Boolean
    fun adapt(value: Raw): Actual
}

interface MutableConfig<Raw, Actual> : Config<Raw, Actual>, ReadWriteProperty<FeatureRemoteConfig, Actual> {
    fun asRaw(value: Actual): Raw
    fun setRawValue(source: ConfigSource, key: String, value: Raw)
}

abstract class AbstractConfig<Conf : Config<Raw, Actual>, Raw, Actual> : Config<Raw, Actual> {

    private lateinit var confKey: String
    private lateinit var sourceClass: KClass<out ConfigSource>

    protected lateinit var defaultValueProvider: () -> Actual

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

    override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        val source = resolveSource(thisRef)

        val key = resolveKey(property)
        val defaultValue = getDefault()

        val value = getRawValue(source, key)
        if (value == null) {
            log("$source - Value not found in remote, using *default* value \"$key\" -> $defaultValue")
            return defaultValue
        }

        if (!isValid(value)) {
            log("$source - Invalid value found in remote, using *default* value \"$key\" -> $defaultValue")
            return defaultValue
        }

        // TODO - Adapt cannot return null on failure, need to find other way represent adaption failure
        val adaptedValue = adapt(value)
        if (adaptedValue == null) {
            log("$source - Failed to adapt value found in remote, using *default* value \"$key\" -> $defaultValue")
            return defaultValue
        }

        return adaptedValue
    }

    protected fun resolveKey(property: KProperty<*>) =
            if (::confKey.isInitialized) confKey else property.name

    protected fun resolveSource(thisRef: FeatureRemoteConfig): ConfigSource {
        val sourceClass = if (::sourceClass.isInitialized) sourceClass else thisRef.source
        return AirConKt.get()!!.configSourceRepository.getSource(sourceClass.java)
    }

    // Need linter here
    override fun getDefault(): Actual {
        if (!::defaultValueProvider.isInitialized) {
            throw RuntimeException("${toString()} - No default value provided!")
        }
        return defaultValueProvider()
    }
}

// TODO - can avoid inheritance here?
abstract class AbstractMutableConfig<Conf : MutableConfig<Raw, Actual>, Raw, Actual> : AbstractConfig<Conf, Raw, Actual>(), MutableConfig<Raw, Actual> {

    override fun setValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, value: Actual) {
        setRawValue(resolveSource(thisRef), resolveKey(property), asRaw(value))
    }
}

abstract class ResourcedConfig<Conf : MutableConfig<Raw, Actual>, Raw, Actual> : AbstractMutableConfig<Conf, Raw, Actual>() {

    fun defaultValue(resource: Resource): Conf {
        defaultValueProvider = {
            adapt(resolve(AirConKt.get()!!.context.resources, resource))
        }
        return this as Conf
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

abstract class SimpleConfig<Conf : MutableConfig<Raw, Raw>, Raw> : AbstractMutableConfig<Conf, Raw, Raw>() {
    override fun isValid(value: Raw) = true

    override fun adapt(value: Raw) = value

    override fun asRaw(value: Raw) = value
}

abstract class SimpleResourcedConfig<Conf : MutableConfig<Raw, Raw>, Raw> : SimpleConfig<Conf, Raw>() {

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