package com.ironsource.aura.aircon.sample.config

import com.ironsource.aura.aircon.config.*
import com.ironsource.aura.aircon.source.FireBaseConfigSource

object CoolKtConfig : FeatureRemoteConfig {
    override val source = FireBaseConfigSource::class

    val someInt by IntConfig()
            .defaultValue(5)
            .validated { it > 7 }
            .adapted { "$it" }

    val someLong by LongConfig()
            .defaultValue(99999999999999L)

    val someFloat by FloatConfig()
            .defaultValue(5f)

    val someString by StringConfig()
            .defaultValue("Default")

    val someBoolean by BooleanConfig()
            .defaultValue(false)
}