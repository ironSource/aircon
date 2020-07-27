package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.constraint.Constraint
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.dsl.AirConDsl
import com.ironsource.aura.airconkt.source.ConfigSource
import com.ironsource.aura.airconkt.utils.toCached
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// This factory exposes API for user custom configs definition
object ConfigPropertyFactory {

    internal fun <Raw, Actual> from(sourceTypeResolver: SourceTypeResolver<Raw>,
                                    validator: (Raw) -> Boolean = { true },
                                    block: AdaptableConfig<Raw, Actual>.() -> Unit):
            ConfigProperty<Actual> =
            ConfigDelegate<Raw, Actual>(sourceTypeResolver, validator).apply(block)

    fun <T> fromPrimitive(sourceTypeResolver: SourceTypeResolver<T>,
                          validator: (T) -> Boolean = { true },
                          block: AdaptableConfig<T, T>.() -> Unit):
            ConfigProperty<T> =
            ConfigDelegate<T, T>(sourceTypeResolver, validator, { it }, { it }).apply(block)

    fun <T> fromNullablePrimitive(sourceTypeResolver: SourceTypeResolver<T>,
                                  validator: (T) -> Boolean = { true },
                                  block: AdaptableConfig<T, T?>.() -> Unit):
            ConfigProperty<T?> =
            ConfigDelegate<T, T?>(sourceTypeResolver, validator, { it }, { it }).apply(block)

    fun <Raw, Actual> from(sourceTypeResolver: SourceTypeResolver<Raw>,
                           validator: (Raw) -> Boolean = { true },
                           getterAdapter: (Raw) -> Actual?,
                           block: AdaptableConfig<Raw, Actual>.() -> Unit):
            ReadOnlyConfigProperty<Actual> =
            ConfigDelegate(sourceTypeResolver, validator, getterAdapter).apply(block)

    fun <Raw, Actual> from(sourceTypeResolver: SourceTypeResolver<Raw>,
                           validator: (Raw) -> Boolean = { true },
                           getterAdapter: (Raw) -> Actual?,
                           setterAdapter: (Actual) -> Raw?,
                           block: AdaptableConfig<Raw, Actual>.() -> Unit):
            ConfigProperty<Actual> =
            ConfigDelegate(sourceTypeResolver, validator, getterAdapter, setterAdapter).apply(block)
}

private class ConfigDelegate<Raw, Actual> internal constructor(private val typeResolver: SourceTypeResolver<Raw>,
                                                               private val validator: (Raw) -> Boolean)
    : ConfigProperty<Actual>,
        ReadOnlyConfigProperty<Actual>,
        AdaptableConfig<Raw, Actual>
//        ,ConfigDelegateApi<Raw, Actual>
{

    constructor(typeResolver: SourceTypeResolver<Raw>,
                validator: (Raw) -> Boolean,
                getterAdapter: (Raw) -> Actual?,
                setterAdapter: (Actual) -> Raw?) :
            this(typeResolver, validator) {
        adapt {
            get(getterAdapter)
            set(setterAdapter)
        }
    }

    constructor(typeResolver: SourceTypeResolver<Raw>,
                validator: (Raw) -> Boolean,
                getterAdapter: (Raw) -> Actual?) :
            this(typeResolver, validator) {
        adapt { get(getterAdapter) }
    }

    override lateinit var key: String
    override lateinit var source: KClass<out ConfigSource>
    override var cached: Boolean = true

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
                val adapted = adapter.getBlock(
                        typeResolver.resourcesResolver.resourcesGetter(AirConKt.context.resources,
                                value))
                adapted ?: throw RuntimeException("Failed to adapt default resource value to type")
            }
        }

    private lateinit var processor: ((Actual) -> Actual)
    private lateinit var adapter: AdapterBuilder<Raw, Actual>
    private lateinit var defaultProvider: () -> Actual

    private var value: Actual? = null
    private var isValueSet: Boolean = false

    private val constraints: MutableList<ConstraintBuilder<Raw, Actual?>> = mutableListOf()

    override fun default(cache: Boolean,
                         provider: () -> Actual) {
        defaultProvider = if (cache) provider.toCached() else provider
    }

    override fun constraint(name: String?,
                            block: Constraint<Raw, Actual?>.() -> Unit) {
        constraints.add(ConstraintBuilder(name, adapter.getBlock, block))
    }

    override fun adapt(block: Adapter<Raw, Actual>.() -> Unit) {
        adapter = AdapterBuilder<Raw, Actual>().apply(block)
    }

    override fun process(processor: (Actual) -> Actual) {
        this.processor = processor
    }

    override fun getValue(thisRef: FeatureRemoteConfig,
                          property: KProperty<*>): Actual {
        // Prepare
        val key = resolveKey(property)
        val source = resolveSource(thisRef)

        // Check cache
        this.value?.let {
            return logAndReturnValue(key, source, it, if (isValueSet) "set" else "cached",
                    "Found cached value")
        }

        // Resolve value
        val value = typeResolver.configSourceResolver.sourceGetter(source, key)
        if (value == null) {
            return logAndReturnValue(key, source, defaultProvider(), "default",
                    "Remote value not found")
        }

        // Internal validation
        if (!validator(value)) {
            return logAndReturnValue(key, source, defaultProvider(), "default",
                    "Internal validation failed")
        }

        // Constraint validation
        constraints.forEach { constraint ->
            val valid = constraint.verify(value)
            if (!valid) {
                val msg = "Constraint ${constraint.name} validation failed"
                val fallbackValue = constraint.fallbackProvider?.invoke(value)
                return if (fallbackValue != null) {
                    logAndReturnValue(key, source, fallbackValue, "fallback", msg)
                } else {
                    logAndReturnValue(key, source, defaultProvider(), "default", msg)
                }
            }
        }

        val adaptedValue = process(key, value)
        if (adaptedValue == null) {
            return logAndReturnValue(key, source, defaultProvider(), "default",
                    "Failed to adapt remote value $value")
        }

        if (cached) {
            this.value = adaptedValue
        }

        return logAndReturnValue(key, source, adaptedValue, "remote", "Remote value configured")
    }

    private fun logAndReturnValue(key: String,
                                  source: ConfigSource,
                                  value: Actual,
                                  type: String,
                                  msg: String): Actual {
        AirConKt.logger?.v("${source::class.simpleName}: $msg - using $type value \"$key\"=$value")
        return value
    }

    private fun process(key: String,
                        value: Raw): Actual? {
        if (!::adapter.isInitialized) {
            throw IllegalStateException(
                    "Failed to get value - no adapter defined for adapted config \"$key\"")
        }
        var adaptedValue = adapter.getBlock(value)
        if (adaptedValue == null) {
            return null
        }

        if (::processor.isInitialized) {
            adaptedValue = processor(adaptedValue)
        }

        return adaptedValue
    }

    override fun setValue(thisRef: FeatureRemoteConfig,
                          property: KProperty<*>,
                          value: Actual) {
        val source = resolveSource(thisRef)
        val key = resolveKey(property)

        if (cached) {
            this.value = value
            isValueSet = true
        }

        AirConKt.logger?.v("${source::class.simpleName}: Setting value \"$key\"=$value")

        typeResolver.configSourceResolver.sourceSetter(source, key, adapter.setBlock(value))
    }

    private fun resolveKey(property: KProperty<*>) =
            if (::key.isInitialized) key else property.name

    private fun resolveSource(thisRef: FeatureRemoteConfig): ConfigSource {
        val sourceClass = if (::source.isInitialized) source else thisRef.source
        return AirConKt.configSourceRepository.getSource(sourceClass)
    }

//    override fun getRawValue(thisRef: FeatureRemoteConfig,
//                             property: KProperty<*>): Raw? {
//        val key = resolveKey(property)
//        val source = resolveSource(thisRef)
//        return typeResolver.configSourceResolver.sourceGetter(source, key)
//    }
}

private fun <T, S> ConstraintBuilder<T, S>.verify(value: T): Boolean {
    verifiers.forEach {
        if (!it(value)) {
            return false
        }
    }

    return true
}

@AirConDsl
interface Adapter<Raw, Actual> {

    fun get(block: (Raw) -> Actual?)
    fun set(block: (Actual) -> Raw?)
}

private class AdapterBuilder<Raw, Actual> : Adapter<Raw, Actual> {
    companion object {
        operator fun <Raw, Actual> invoke(block: Adapter<Raw, Actual>.() -> Unit) =
                AdapterBuilder<Raw, Actual>().apply(block)
    }

    lateinit var getBlock: (Raw) -> Actual?
    lateinit var setBlock: (Actual) -> Raw?

    override fun get(block: (Raw) -> Actual?) {
        getBlock = block
    }

    override fun set(block: (Actual) -> Raw?) {
        setBlock = block
    }
}