package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import kotlin.reflect.KProperty

//fun <Raw, Actual, Adapted> ReadOnlyConfig<Raw, Actual>.adapted(adapter: (Actual) -> Adapted) =
//        object : ReadOnlyConfigDelegate<Raw, Adapted>() {
//
//            init {
//                // Copy default value from old config since it can be defined before
//                // call to adapted()
//                if (this@adapted.hasDefaultValue()) {
//                    defaultValue { adapter(this@adapted.defaultValue()) }
//                }
//            }
//
//            // In practice, once this method is overridden and delegated to the old config
//            // non of the other overrides matter
//            override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): Adapted {
//                val remoteValue = this@adapted.getRemoteValue(thisRef, property) { getDefaultValue() }
//                return if (remoteValue != null) adapter(remoteValue) else getDefaultValue()
//            }
//        }
//
////// TODO renamed since it clashes with the adapted function, need to rethink
////fun <Raw, Actual, Adapted> MutableConfig<Raw, Actual>.adaptedMutable(adapter: (Actual) -> Adapted, serializer: (Adapted) -> Raw): MutableConfig<Raw, Adapted> {
////    return object : MutableConfig<MutableConfig<Raw, Adapted>, Raw, Adapted>() {
////
////        override fun getRawValue(source: ConfigSource, key: String) = this@adaptedMutable.getRawValue(source, key)
////
////        override fun setRawValue(source: ConfigSource, key: String, value: Raw) {
////            this@adaptedMutable.setRawValue(source, key, value)
////        }
////
////        override fun asRaw(value: Adapted) = serializer(value)
////
////        override fun adapt(value: Raw) = adapter(this@adaptedMutable.adapt(value))
////
////        override fun isValid(value: Raw) = this@adaptedMutable.isValid(value)
////    }
////}