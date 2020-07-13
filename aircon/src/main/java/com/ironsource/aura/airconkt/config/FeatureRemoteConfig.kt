package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.reflect.KClass

interface FeatureRemoteConfig {
    val source: KClass<out ConfigSource>
}