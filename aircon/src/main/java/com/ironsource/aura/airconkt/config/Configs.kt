package com.ironsource.aura.airconkt.config

import android.content.res.Resources
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

// TODO - nullable types
// TODO - @Mutable
// TODO - Enum types, json type, duration type
// TODO - constraints (min, max..)
interface Config<Raw, Actual> : ReadOnlyProperty<FeatureRemoteConfig, Actual> {
    fun getDefault(): Actual
    fun getRawValue(source: ConfigSource, key: String): Raw?
    fun isValid(value: Raw): Boolean
    fun adapt(value: Raw): Actual
}

abstract class AbstractConfig<Raw, Actual> : Config<Raw, Actual> {

    lateinit var key: String
    lateinit var source: KClass<out ConfigSource>

    protected lateinit var defaultValueProvider: () -> Actual

    fun defaultValue(value: Actual): AbstractConfig<Raw, Actual> {
        defaultValueProvider = { value }
        return this
    }

    fun defaultValue(provider: () -> Actual): AbstractConfig<Raw, Actual> {
        defaultValueProvider = provider
        return this
    }

    override fun getValue(thisRef: FeatureRemoteConfig, property: kotlin.reflect.KProperty<*>): Actual {
        val sourceClass = if (::source.isInitialized) source else thisRef.source
        val key = if (::key.isInitialized) key else property.name
        val defaultValue = getDefault()

        val source = AirConKt.get()!!.configSourceRepository.getSource(sourceClass.java)

        val value = getRawValue(source, key)
        if (value == null) {
            log("$sourceClass - Value not found in remote, using *default* value \"$key\" -> $defaultValue")
            return defaultValue
        }

        if (!isValid(value)) {
            log("$sourceClass - Invalid value found in remote, using *default* value \"$key\" -> $defaultValue")
            return defaultValue
        }

        // TODO - Adapt cannot return null on failure, need to find other way represent adaption failure
        val adaptedValue = adapt(value)
        if (adaptedValue == null) {
            log("$sourceClass - Failed to adapt value found in remote, using *default* value \"$key\" -> $defaultValue")
            return defaultValue
        }

        return adaptedValue
    }

    override fun getDefault() = defaultValueProvider()
}

abstract class ResourcedConfig<Raw, Actual> : AbstractConfig<Raw, Actual>() {

    fun defaultValue(resource: Resource): ResourcedConfig<Raw, Actual> {
        defaultValueProvider = {
            adapt(resolve(AirConKt.get()!!.context.resources, resource))
        }
        return this
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

abstract class SimpleConfig<Raw> : AbstractConfig<Raw, Raw>() {
    override fun isValid(value: Raw) = true

    override fun adapt(value: Raw) = value
}

abstract class SimpleResourcedConfig<Raw> : SimpleConfig<Raw>() {

    fun defaultValue(resource: Resource): SimpleResourcedConfig<Raw> {
        defaultValueProvider = { resolve(AirConKt.get()!!.context.resources, resource) }
        return this
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

class IntConfig : SimpleResourcedConfig<Int>() {
    override fun resolve(resources: Resources, resource: Resource): Int {
        return resources.getInteger(resource.resId)
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getInteger(key)
}

class LongConfig : SimpleResourcedConfig<Long>() {
    override fun resolve(resources: Resources, resource: Resource): Long {
        return resources.getInteger(resource.resId).toLong()
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getLong(key)
}

class FloatConfig : SimpleResourcedConfig<Float>() {
    override fun resolve(resources: Resources, resource: Resource): Float {
        //TODO
        return 0f
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getFloat(key)
}

open class StringConfig : SimpleResourcedConfig<String>() {
    override fun resolve(resources: Resources, resource: Resource): String {
        return resources.getString(resource.resId)
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getString(key)
}

class BooleanConfig : SimpleResourcedConfig<Boolean>() {
    override fun resolve(resources: Resources, resource: Resource): Boolean {
        return resources.getBoolean(resource.resId)
    }

    override fun getRawValue(source: ConfigSource, key: String) =
            source.getBoolean(key)
}

private fun log(msg: String) {
    AirConKt.get()
            .logger
            .v(msg)
}