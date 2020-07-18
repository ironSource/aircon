package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.source.ConfigSource
import com.ironsource.aura.airconkt.utils.toCached
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

open class ConfigDelegate<Raw, Actual> protected constructor(private val typeResolver: SourceTypeResolver<Raw>,
                                                             private val validator: (Raw) -> Boolean) : Config<Raw, Actual> {

    protected constructor(typeResolver: SourceTypeResolver<Raw>,
                          validator: (Raw) -> Boolean,
                          adapter: (Raw) -> Actual?,
                          serializer: (Actual) -> Raw?) :
            this(typeResolver, validator) {
        adapt(adapter)
        serialize(serializer)
    }

    companion object {
        operator fun <Raw, Actual> invoke(sourceTypeResolver: SourceTypeResolver<Raw>,
                                          validator: (Raw) -> Boolean = { true },
                                          block: Config<Raw, Actual>.() -> Unit) =
                ConfigDelegate<Raw, Actual>(sourceTypeResolver, validator).apply(block)

        operator fun <Raw, Actual> invoke(sourceTypeResolver: SourceTypeResolver<Raw>,
                                          validator: (Raw) -> Boolean = { true },
                                          adapter: (Raw) -> Actual?,
                                          serializer: (Actual) -> Raw?,
                                          block: Config<Raw, Actual>.() -> Unit) =
                ConfigDelegate(sourceTypeResolver, validator, adapter, serializer).apply(block)
    }

    override lateinit var key: String
    override lateinit var source: KClass<out ConfigSource>
    override var cacheValue: Boolean = true

    override var default: Actual
        get() = defaultProvider()
        set(value) {
            default { value }
        }

    override var defaultRes: Int
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            default {
                val adapted = adapter(typeResolver.resourcesResolver.resourcesGetter(AirConKt.context.resources, value))
                adapted ?: throw RuntimeException("Failed to adapt default resource value to type")
            }
        }

    private lateinit var processor: ((Actual) -> Actual)
    private lateinit var adapter: (Raw) -> Actual?
    private lateinit var serializer: (Actual) -> Raw?
    private lateinit var defaultProvider: () -> Actual

    private var value: Actual? = null

    private val constraints: MutableList<ConstraintBuilder<Raw, Actual?>> = mutableListOf()

    override fun default(cache: Boolean, provider: () -> Actual) {
        defaultProvider = if (cache) provider.toCached() else provider
    }

    override fun constraint(name: String?,
                            block: ConstraintBuilder<Raw, Actual?>.() -> Unit) {
        constraints.add(ConstraintBuilder(name, adapter, block))
    }

    final override fun adapt(adapter: (Raw) -> Actual?) {
        this.adapter = adapter
    }

    final override fun serialize(serializer: (Actual) -> Raw?) {
        this.serializer = serializer
    }

    override fun process(processor: (Actual) -> Actual) {
        this.processor = processor
    }

    override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        // Prepare
        val key = resolveKey(property)
        val source = resolveSource(thisRef)

        // Check cache
        this.value?.let {
            return logAndReturnValue(key, it, "cached", "Found cached value")
        }

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

        if (cacheValue) {
            this.value = adaptedValue
        }

        return logAndReturnValue(key, adaptedValue, "remote", "Remote value configured")
    }

    private fun logAndReturnValue(key: String, value: Actual, type: String, msg: String): Actual {
        AirConKt.logger?.v("$source: $msg - using $type value \"$key\"=$value")
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
        return AirConKt.configSourceRepository.getSource(sourceClass.java)
    }

    internal fun getRawValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Raw? {
        val key = resolveKey(property)
        val source = resolveSource(thisRef)
        return typeResolver.configSourceResolver.sourceGetter(source, key)
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