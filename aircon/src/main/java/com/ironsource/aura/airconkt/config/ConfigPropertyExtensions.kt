package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import kotlin.reflect.KProperty1

@Suppress("UNCHECKED_CAST")
fun <FeatureRemoteConfigType : FeatureRemoteConfig, Actual>
        KProperty1<FeatureRemoteConfigType, Actual>.getDefaultValue(
        instance: FeatureRemoteConfigType
) = getConfigDelegate(instance).default as Actual

@Suppress("UNCHECKED_CAST")
fun <FeatureRemoteConfigType : FeatureRemoteConfig, Raw>
        KProperty1<FeatureRemoteConfigType, *>.getRawValue(
        instance: FeatureRemoteConfigType
) = getConfigDelegate(instance).getRawValue(instance, this) as Raw?

fun <FeatureRemoteConfigType : FeatureRemoteConfig>
        KProperty1<FeatureRemoteConfigType, *>.isConfigured(
        instance: FeatureRemoteConfigType
) = getConfigDelegate(instance).getRawValue(instance, this) != null

private fun <FeatureRemoteConfigType : FeatureRemoteConfig>
        KProperty1<FeatureRemoteConfigType, *>.getConfigDelegate(
        instance: FeatureRemoteConfigType): ConfigDelegate<*, *> {
    val delegate = getDelegate(instance)
    if (delegate !is ConfigDelegate<*, *>) {
        throw IllegalStateException("This function can only be called on config properties")
    }
    return delegate
}