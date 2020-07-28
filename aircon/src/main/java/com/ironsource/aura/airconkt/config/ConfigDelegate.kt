package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.constraint.Constraint
import com.ironsource.aura.airconkt.config.constraint.ConstraintBuilder
import com.ironsource.aura.airconkt.dsl.AirConDsl
import com.ironsource.aura.airconkt.source.ConfigSource
import com.ironsource.aura.airconkt.utils.toCached
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
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

private class ConfigDelegate<Raw, Actual> internal constructor(typeResolver: SourceTypeResolver<Raw>,
                                                               validator: (Raw) -> Boolean)
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

    // Base
    override lateinit var key: String
    override lateinit var source: KClass<out ConfigSource>
    override var cached: Boolean = true

    // Disposables - nullified after first cached get
    private var typeResolver: SourceTypeResolver<Raw>? = typeResolver
    private var validator: ((Raw) -> Boolean)? = validator
    private var processor: ((Actual) -> Actual)? = null
    private var adapter: AdapterBuilder<Raw, Actual>? = null
    private var defaultProvider: (() -> Actual)? = null
    private var constraints: MutableList<ConstraintBuilder<Raw, Actual?>>? = mutableListOf()

    // Cache
    private var value: Actual? = null
    private var valueSet: Boolean = false

    private val getterAdapter
        get() = adapter!!.getter!!

    private val setterAdapter
        get() = adapter!!.setter!!

    override var default: Actual
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            default { value }
        }

    override var defaultRes: Int
        @Deprecated("", level = DeprecationLevel.ERROR)
        get() = throw UnsupportedOperationException()
        set(value) {
            default {
                val adapted = getterAdapter(
                        typeResolver!!.resourcesResolver.resourcesGetter(AirConKt.context.resources,
                                value))
                adapted ?: throw RuntimeException("Failed to adapt default resource value to type")
            }
        }

    override fun default(cache: Boolean,
                         provider: () -> Actual) {
        defaultProvider = if (cache) provider.toCached() else provider
    }

    override fun constraint(name: String?,
                            block: Constraint<Raw, Actual?>.() -> Unit) {
        constraints!!.add(ConstraintBuilder(name, getterAdapter, block))
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
            return cacheAndReturn(property, key, source, it, if (valueSet) "set" else "cached",
                    "Found cached value")
        }

        assertRequiredGetterValues(property)

        // Resolve value
        val value = typeResolver!!.configSourceResolver.sourceGetter(source, key)
        if (value == null) {
            return cacheAndReturn(property, key, source, resolveDefault(), "default",
                    "Remote value not found")
        }

        // Internal validation
        if (!validator!!(value)) {
            return cacheAndReturn(property, key, source, resolveDefault(), "default",
                    "Internal validation failed")
        }

        // Constraint validation
        constraints!!.forEach { constraint ->
            val valid = constraint.verify(value)
            if (!valid) {
                val msg = "Constraint ${constraint.name} validation failed"
                val fallbackValue = constraint.fallbackProvider?.invoke(value)
                return if (fallbackValue != null) {
                    cacheAndReturn(property, key, source, fallbackValue, "fallback", msg)
                } else {
                    cacheAndReturn(property, key, source, resolveDefault(), "default", msg)
                }
            }
        }

        val adaptedValue = process(key, value)
        if (adaptedValue == null) {
            return cacheAndReturn(property, key, source, resolveDefault(), "default",
                    "Failed to adapt remote value $value")
        }

        return cacheAndReturn(property, key, source, adaptedValue, "remote", "Remote value configured")
    }

    private fun assertRequiredGetterValues(property: KProperty<*>) {
        checkNotNull(property, defaultProvider, "get", "default value")
        checkNotNull(property, adapter?.getter, "get", "adapter")
    }

    private fun process(key: String,
                        value: Raw): Actual? {
        var adaptedValue = getterAdapter(value)
        if (adaptedValue == null) {
            return null
        }

        if (processor != null) {
            adaptedValue = processor!!(adaptedValue)
        }

        return adaptedValue
    }

    private fun resolveDefault() = defaultProvider!!()

    private fun cacheAndReturn(property: KProperty<*>,
                               key: String,
                               source: ConfigSource,
                               value: Actual,
                               type: String,
                               msg: String): Actual {
        AirConKt.logger?.v("${source::class.simpleName}: $msg - using $type value \"$key\"=$value")

        if (cached) {
            setCache(property, value)
        }

        return value
    }

    override fun setValue(thisRef: FeatureRemoteConfig,
                          property: KProperty<*>,
                          value: Actual) {
        assertRequiredSetterValues(property)

        val source = resolveSource(thisRef)
        val key = resolveKey(property)

        AirConKt.logger?.v("${source::class.simpleName}: Setting value \"$key\"=$value")

        typeResolver!!.configSourceResolver.sourceSetter(source, key, setterAdapter(value))

        if (cached) {
            setCache(property, value)
            valueSet = true
        }
    }

    private fun assertRequiredSetterValues(property: KProperty<*>) {
        checkNotNull(property, adapter?.setter, "set", "setter adapter")
    }

    private fun setCache(property: KProperty<*>,
                         value: Actual?) {
        this.value = value
        cleanup(property)
    }

    // Nullify no longer used properties to save memory
    private fun cleanup(property: KProperty<*>) {
        mutableCleanup()

        // Adapter
        if (property !is KMutableProperty<*>) {
            readOnlyCleanup()
        }
    }

    private fun resolveKey(property: KProperty<*>) =
            if (::key.isInitialized) key else property.name

    private fun resolveSource(thisRef: FeatureRemoteConfig): ConfigSource {
        val sourceClass = if (::source.isInitialized) source else thisRef.source
        return AirConKt.configSourceRepository.getSource(sourceClass)
    }

    private fun mutableCleanup() {
        processor = null
        validator = null
        defaultProvider = null
        constraints = null
    }

    private fun readOnlyCleanup() {
        typeResolver = null
        adapter = null
    }

    private fun checkNotNull(property: KProperty<*>,
                             value: Any?,
                             opName: String,
                             name: String) {
        checkNotNull(value) {
            "Failed to $opName value - no $name provided for config \"${resolveKey(property)}\""
        }
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

    internal var getter: ((Raw) -> Actual?)? = null
    internal var setter: ((Actual) -> Raw?)? = null

    override fun get(block: (Raw) -> Actual?) {
        getter = block
    }

    override fun set(block: (Actual) -> Raw?) {
        setter = block
    }
}