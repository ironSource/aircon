package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.source.ConfigSource
import kotlin.reflect.KClass

interface FeatureRemoteConfig {
    val source: KClass<out ConfigSource>
}