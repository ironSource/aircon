package com.ironsource.aura.airconkt.sample.config

import com.ironsource.aura.airconkt.config.*
import com.ironsource.aura.airconkt.source.FireBaseConfigSource

object CoolKtConfig : FeatureRemoteConfig {
    override val source = FireBaseConfigSource::class

    var someInt by IntConfig()
            .defaultValue(20)

    val someLong by LongConfig()
            .defaultValue(99999999999999L)

    val someFloat by FloatConfig()
            .defaultValue(5f)

    val someString by StringConfig()
            .defaultValue("Default")

    val someBoolean by BooleanConfig()
            .defaultValue(false)

    val someNullableString by StringConfig()

    val someList by JsonConfig.create<List<String>>()

    val test by IntConfig()
            .constraint { it > 6 }
            .range(min = 15)
            .adapted { "" }
}