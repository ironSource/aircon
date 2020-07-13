package com.ironsource.aura.aircon.config

import android.content.res.Resources
import com.ironsource.aura.aircon.AirCon
import com.ironsource.aura.aircon.source.ConfigSource
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

// RawODO - nullable types
interface Config<Raw, Actual> : ReadOnlyProperty<FeatureRemoteConfig, Actual> {
    fun getDefault(): Raw
}

abstract class AbstractConfig<Raw, Actual> : Config<Raw, Actual> {

    lateinit var key: String
    lateinit var source: KClass<out ConfigSource>

    protected lateinit var defaultValueProvider: () -> Raw

    fun defaultValue(value: Raw): AbstractConfig<Raw, Actual> {
        defaultValueProvider = { value }
        return this
    }

    fun defaultValue(provider: () -> Raw): AbstractConfig<Raw, Actual> {
        defaultValueProvider = provider
        return this
    }

    override fun getDefault() = defaultValueProvider()

    override fun getValue(thisRef: FeatureRemoteConfig, property: kotlin.reflect.KProperty<*>): Actual {
        val sourceClass = if (::source.isInitialized) source else thisRef.source
        val key = if (::key.isInitialized) key else property.name
        val defaultValue = getDefault()

        val source = AirCon.get()!!.configSourceRepository.getSource(sourceClass.java)

        var value = getValue(source, key, defaultValue)
        value = if (value != null && isValid(value)) value else defaultValue
        return adapt(value)
    }

    protected open fun isValid(value: Raw) = true

    abstract fun getValue(source: ConfigSource, key: String, defaultValue: Raw): Raw?

    abstract fun adapt(value: Raw): Actual
}

abstract class ResourcedConfig<Raw, Actual> : AbstractConfig<Raw, Actual>() {

    fun defaultValue(resource: Resource): ResourcedConfig<Raw, Actual> {
        defaultValueProvider = { resolve(AirCon.get()!!.context.resources, resource) }
        return this
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

abstract class SimpleConfig<Raw> : AbstractConfig<Raw, Raw>() {
    override fun adapt(value: Raw) = value
}

abstract class SimpleResourcedConfig<Raw> : SimpleConfig<Raw>() {

    fun defaultValue(resource: Resource): SimpleResourcedConfig<Raw> {
        defaultValueProvider = { resolve(AirCon.get()!!.context.resources, resource) }
        return this
    }

    abstract fun resolve(resources: Resources, resource: Resource): Raw
}

class IntConfig : SimpleResourcedConfig<Int>() {
    override fun resolve(resources: Resources, resource: Resource): Int {
        return resources.getInteger(resource.resId)
    }

    override fun getValue(source: ConfigSource, key: String, defaultValue: Int) = source.getInteger(key, defaultValue)
}

class LongConfig : SimpleResourcedConfig<Long>() {
    override fun resolve(resources: Resources, resource: Resource): Long {
        return resources.getInteger(resource.resId).toLong()
    }

    override fun getValue(source: ConfigSource, key: String, defaultValue: Long) = source.getLong(key, defaultValue)
}

class FloatConfig : SimpleResourcedConfig<Float>() {
    override fun resolve(resources: Resources, resource: Resource): Float {
        //RawODO
        return 0f
    }

    override fun getValue(source: ConfigSource, key: String, defaultValue: Float) = source.getFloat(key, defaultValue)
}

open class StringConfig : SimpleResourcedConfig<String>() {
    override fun resolve(resources: Resources, resource: Resource): String {
        return resources.getString(resource.resId)
    }

    override fun getValue(source: ConfigSource, key: String, defaultValue: String) = source.getString(key, defaultValue)
}

class BooleanConfig : SimpleResourcedConfig<Boolean>() {
    override fun resolve(resources: Resources, resource: Resource): Boolean {
        return resources.getBoolean(resource.resId)
    }

    override fun getValue(source: ConfigSource, key: String, defaultValue: Boolean) =
            source.getBoolean(key, defaultValue)
}