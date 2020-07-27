@file:Suppress("UNCHECKED_CAST")

package com.ironsource.aura.airconkt.config

// TODO - need to rethink this impl since kotlin-reflect adds 600KB
//import kotlin.reflect.KProperty
//import kotlin.reflect.KProperty1
//import kotlin.reflect.jvm.isAccessible
//
//interface ConfigPropertyApi<Raw, Actual> {
//    fun getDefaultValue(): Actual
//    fun isConfigured(): Boolean
//    fun getIntRawValue(): Int?
//    fun getLongRawValue(): Long?
//    fun getFloatRawValue(): Float?
//    fun getStringRawValue(): String?
//    fun getBooleanRawValue(): Boolean?
//    fun getAnyRawValue(): Any?
//}
//
//internal interface ConfigDelegateApi<Raw, Actual> {
//    val default: Actual
//
//    fun getRawValue(thisRef: FeatureRemoteConfig,
//                    property: KProperty<*>): Raw?
//}
//
//private class ConfigPropertyApiImpl<FeatureRemoteConfigType : FeatureRemoteConfig, Raw, Actual>
//(private val instance: FeatureRemoteConfigType,
// private val property: KProperty1<FeatureRemoteConfigType, Actual>,
// private val configDelegate: ConfigDelegateApi<Raw, Actual>) :
//        ConfigPropertyApi<Raw, Actual> {
//
//    override fun getDefaultValue() = configDelegate.default
//
//    override fun isConfigured() = getRawValue<Any?>() != null
//
//    override fun getIntRawValue() = getRawValue<Int>()
//
//    override fun getLongRawValue() = getRawValue<Long>()
//
//    override fun getFloatRawValue() = getRawValue<Float>()
//
//    override fun getStringRawValue() = getRawValue<String>()
//
//    override fun getBooleanRawValue() = getRawValue<Boolean>()
//
//    override fun getAnyRawValue() = getRawValue<Any?>()
//
//    private fun <Raw> getRawValue() = configDelegate.getRawValue(instance, property) as Raw?
//}
//
//fun <FeatureRemoteConfigType : FeatureRemoteConfig, Actual>
//        KProperty1<FeatureRemoteConfigType, Actual>.asConfigProperty(
//        instance: FeatureRemoteConfigType): ConfigPropertyApi<*, Actual> =
//        ConfigPropertyApiImpl(instance, this, this.getConfigDelegate(instance))
//
//private fun <FeatureRemoteConfigType : FeatureRemoteConfig, Actual>
//        KProperty1<FeatureRemoteConfigType, Actual>.getConfigDelegate(
//        instance: FeatureRemoteConfigType): ConfigDelegateApi<*, Actual> {
//    isAccessible = true
//    val delegate = getDelegate(instance)
//    if (delegate !is ConfigDelegateApi<*, *>) {
//        throw IllegalStateException("This function can only be called on config properties")
//    }
//    return delegate as ConfigDelegateApi<*, Actual>
//}