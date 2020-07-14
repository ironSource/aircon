package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.reflect.KProperty

fun <Raw, Actual, Adapted, Conf : Config<Raw, Actual, Conf>>
        Conf.adapted(adapter: (Actual) -> Adapted) =
        AdaptedConfig(this, adapter)

class AdaptedConfig<Raw, OldActual, Actual,
        OldConf : Config<Raw, OldActual, OldConf>>
(private val oldConf: OldConf,
 private val adapter: (OldActual) -> Actual)
    : Config<Raw, Actual, AdaptedConfig<Raw, OldActual, Actual, *>>() {

    init {
        // Copy default value from old config since it can be defined before
        // call to adapted()
        if (oldConf.hasDefaultValue()) {
            defaultValue { adapter(oldConf.defaultValueProvider()) }
        }
    }

    // Not relevant - see getValue()
    override fun getRawValue(source: ConfigSource, key: String) = null

    // Not relevant - see getValue()
    override fun isValid(value: Raw) = false

    // Not relevant - see getValue()
    override fun adapt(value: Raw) = adapter(oldConf.adapt(value))

    // In practice, once this method is overridden and delegated to the old config
    // non of the other overrides matter
    override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Actual {
        val remoteValue = oldConf.getRemoteValue(thisRef, property) { getDefaultValue() }
        return if (remoteValue != null) adapter(remoteValue) else getDefaultValue()
    }
}

//// TODO renamed since it clashes with the adapted function, need to rethink
//fun <Raw, Actual, Adapted> MutableConfig<Raw, Actual>.adaptedMutable(adapter: (Actual) -> Adapted, serializer: (Adapted) -> Raw): MutableConfig<Raw, Adapted> {
//    return object : MutableConfig<MutableConfig<Raw, Adapted>, Raw, Adapted>() {
//
//        override fun getRawValue(source: ConfigSource, key: String) = this@adaptedMutable.getRawValue(source, key)
//
//        override fun setRawValue(source: ConfigSource, key: String, value: Raw) {
//            this@adaptedMutable.setRawValue(source, key, value)
//        }
//
//        override fun asRaw(value: Adapted) = serializer(value)
//
//        override fun adapt(value: Raw) = adapter(this@adaptedMutable.adapt(value))
//
//        override fun isValid(value: Raw) = this@adaptedMutable.isValid(value)
//    }
//}