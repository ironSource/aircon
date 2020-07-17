package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// TODO - adapting custom configs (e.g enum to other)
// TODO - nullable types (currently string can be used with String? property but default value cannot be null)
// TODO - sealed class enum? (need to think about inheritors with constructor)
// TODO - caching option (defaultValue, final value...)
// TODO - proguard (R8) rules - remote config properties names should not be touched
// TODO - aux methods (isConfigured, getRawValue, getDefaultValue)...

// TODO ONGOING - builtin constraints (e.g acceptedValues)

open class ConfigDelegate<Raw, Actual> protected constructor(private val typeResolver: TypeResolver<Raw>,
                                                             private val validator: (Raw) -> Boolean)
    : Config<Raw, Actual> {

    protected constructor(typeResolver: TypeResolver<Raw>,
                          validator: (Raw) -> Boolean,
                          adapter: (Raw) -> Actual?,
                          serializer: (Actual) -> Raw) :
            this(typeResolver, validator) {
        adapt(adapter)
        serialize(serializer)
    }

    companion object {
        operator fun <Raw, Actual> invoke(typeResolver: TypeResolver<Raw>,
                                          validator: (Raw) -> Boolean = { true },
                                          block: Config<Raw, Actual>.() -> Unit) =
                ConfigDelegate<Raw, Actual>(typeResolver, validator).apply(block)

        operator fun <Raw, Actual> invoke(typeResolver: TypeResolver<Raw>,
                                          validator: (Raw) -> Boolean = { true },
                                          adapter: (Raw) -> Actual?,
                                          serializer: (Actual) -> Raw,
                                          block: Config<Raw, Actual>.() -> Unit) =
                ConfigDelegate(typeResolver, validator, adapter, serializer).apply(block)
    }

    override lateinit var key: String
    override lateinit var source: KClass<out ConfigSource>

    private lateinit var processor: ((Actual) -> Actual)
    private lateinit var adapter: (Raw) -> Actual?
    private lateinit var serializer: (Actual) -> Raw

    override var default: Actual
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            defaultProvider = { value }
        }

    override var defaultRes: Int
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            defaultProvider = {
                val adapted = adapter(typeResolver.resourcesResolver.resourcesGetter(AirConKt.get()!!.context.resources, value))
                adapted ?: throw RuntimeException("Failed to adapt default resource value to type")
            }
        }

    internal lateinit var defaultProvider: () -> Actual

    private val constraints: MutableList<ConstraintBuilder<Raw, Actual?>> = mutableListOf()

    override fun default(provider: () -> Actual) {
        defaultProvider = provider
    }

    override fun constraint(name: String?,
                            block: ConstraintBuilder<Raw, Actual?>.() -> Unit) {
        constraints.add(ConstraintBuilder(name, adapter, block))
    }

    final override fun adapt(adapter: (Raw) -> Actual?) {
        this.adapter = adapter
    }

    final override fun serialize(serializer: (Actual) -> Raw) {
        this.serializer = serializer
    }

    override fun process(processor: (Actual) -> Actual) {
        this.processor = processor
    }

    override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        return getValue(thisRef, property) { defaultProvider() }
    }

    internal fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, defaultProvider: () -> Actual): Actual {
        // Prepare
        val key = resolveKey(property)
        val source = resolveSource(thisRef)

        // Resolve value
        val value = typeResolver.configSourceResolver.sourceGetter(source, key)
        if (value == null) {
            return logAndReturnValue(key, defaultProvider(), "default", "Remote value not found")
        }

        // Internal validation
        if (!validator(value)) {
            return logAndReturnValue(key, defaultProvider(), "default", "Internal validation failed")
        }

        // Constraint validation
        constraints.forEach { constraint ->
            val valid = constraint.verify(value)
            if (!valid) {
                val msg = "Constraint ${constraint.name} validation failed"
                val fallbackValue = constraint.fallbackProvider?.invoke(value)
                return if (fallbackValue != null) {
                    logAndReturnValue(key, fallbackValue, "fallback", msg)
                } else {
                    logAndReturnValue(key, defaultProvider(), "default", msg)
                }
            }
        }

        val adaptedValue = process(key, value)
        if (adaptedValue == null) {
            return logAndReturnValue(key, defaultProvider(), "default", "Failed to adapt remote value $value")
        }

        return logAndReturnValue(key, adaptedValue, "remote", "Remote value configured")
    }

    private fun logAndReturnValue(key: String, value: Actual, type: String, msg: String): Actual {
        log("$source: $msg - using $type value \"$key\"=$value")
        return value
    }

    private fun process(key: String, value: Raw): Actual? {
        if (!::adapter.isInitialized) {
            throw IllegalStateException("Failed to get value - no adapter defined for adapted config \"$key\"")
        }
        var adaptedValue = adapter(value)
        if (adaptedValue == null) {
            return null
        }

        if (::processor.isInitialized) {
            adaptedValue = processor(adaptedValue)
        }

        return adaptedValue
    }

    override fun setValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, value: Actual) {
        val source = resolveSource(thisRef)
        val key = resolveKey(property)

        if (!::serializer.isInitialized) {
            throw IllegalStateException("Failed to set value - no serializer defined for adapted config \"$key\"")
        }

        typeResolver.configSourceResolver.sourceSetter(source, key, serializer(value))
    }

    private fun resolveKey(property: KProperty<*>) =
            if (::key.isInitialized) key else property.name

    private fun resolveSource(thisRef: FeatureRemoteConfig): ConfigSource {
        val sourceClass = if (::source.isInitialized) source else thisRef.source
        return AirConKt.get()!!.configSourceRepository.getSource(sourceClass.java)
    }
}

private fun <T, S> ConstraintBuilder<T, S>.verify(value: T): Boolean {
    verifiers.forEach {
        if (!it(value)) {
            return false
        }
    }

    return true
}

typealias SimpleConfig<T> = Config<T, T>

private fun log(msg: String) {
    AirConKt.get()
            .logger
            .v(msg)
}