@file:Suppress("UNCHECKED_CAST")

package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

class ConfigPropertyApi<FeatureRemoteConfigType : FeatureRemoteConfig, Raw, Actual>
(private val instance: FeatureRemoteConfigType,
 private val property: KProperty1<FeatureRemoteConfigType, Actual>,
 private val configDelegate: ConfigDelegate<Raw, Actual>) {

    fun getDefaultValue() = configDelegate.default

    fun isConfigured() = getRawValue<Any?>() != null

    fun getIntRawValue() = getRawValue<Int>()

    fun getLongRawValue() = getRawValue<Long>()

    fun getFloatRawValue() = getRawValue<Float>()

    fun getStringRawValue() = getRawValue<String>()

    fun getBooleanRawValue() = getRawValue<Boolean>()

    private fun <Raw> getRawValue() = configDelegate.getRawValue(instance, property) as Raw?
}

fun <FeatureRemoteConfigType : FeatureRemoteConfig, Actual>
        KProperty1<FeatureRemoteConfigType, Actual>.asConfigProperty(
        instance: FeatureRemoteConfigType
) = ConfigPropertyApi(instance, this, this.getConfigDelegate(instance))

private fun <FeatureRemoteConfigType : FeatureRemoteConfig, Actual>
        KProperty1<FeatureRemoteConfigType, Actual>.getConfigDelegate(
        instance: FeatureRemoteConfigType): ConfigDelegate<*, Actual> {
    isAccessible = true
    val delegate = getDelegate(instance)
    if (delegate !is ConfigDelegate<*, *>) {
        throw IllegalStateException("This function can only be called on config properties")
    }
    return delegate as ConfigDelegate<*, Actual>
}