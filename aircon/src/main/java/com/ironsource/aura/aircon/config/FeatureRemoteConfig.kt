package com.ironsource.aura.aircon.config

import com.ironsource.aura.aircon.source.ConfigSource
import kotlin.reflect.KClass

interface FeatureRemoteConfig {
    val source: KClass<out ConfigSource>
}