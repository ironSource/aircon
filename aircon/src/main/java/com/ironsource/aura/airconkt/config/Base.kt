package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// TODO - revive adapted
// TODO - nullable types (currently string can be used with String? property but default value cannot be null)
// TODO - Enum types, duration type
// TODO - more builtin constraints (e.g acceptedValues)
// TODO - caching option (defaultValue, final value...)

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
                val adapted = adapter(resourcesResolver.resourcesGetter(AirConKt.get()!!.context.resources, value))
                adapted ?: throw RuntimeException("Failed to adapt default resource value to type")
            }
        }

    internal lateinit var defaultProvider: () -> Actual

    private val constraints: MutableList<ConstraintBuilder<Raw, Actual>> = mutableListOf()

    override fun default(provider: () -> Actual) {
        defaultProvider = provider
    }

    override fun constraint(name: String?,
                            block: ConstraintBuilder<Raw, Actual>.() -> Unit) {
        constraints.add(ConstraintBuilder(name, adapter, block))
    }

    override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        return getValue(thisRef, property) { defaultProvider() }
    }

    internal fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>, defaultProvider: () -> Actual): Actual {
        // Prepare
        val key = resolveKey(property)
        val source = resolveSource(thisRef)

        // Resolve value
        val value = configSourceResolver.sourceGetter(source, key)
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

        val adaptedValue = process(value)
        if (adaptedValue == null) {
            return logAndReturnValue(key, defaultProvider(), "default", "Failed to adapt remote value $value")
        }

        return logAndReturnValue(key, adaptedValue, "remote", "Remote value configured")
    }

    private fun logAndReturnValue(key: String, value: Actual, type: String, msg: String): Actual {
        log("$source: $msg - using $type value \"$key\"=$value")
        return value
    }

    private fun process(value: Raw): Actual? {
        var adaptedValue = adapter(value)
        if (adaptedValue == null) {
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

    internal fun hasDefaultValue() = ::defaultProvider.isInitialized
}

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

private fun <T, S> ConstraintBuilder<T, S>.verify(value: T): Boolean {
    verifiers.forEach {
        if (!it(value)) {
            return false
        }
    }

    return true
}

private fun log(msg: String) {
    AirConKt.get()
            .logger
            .v(msg)
}