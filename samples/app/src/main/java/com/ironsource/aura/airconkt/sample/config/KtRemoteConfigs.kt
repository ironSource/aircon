package com.ironsource.aura.airconkt.sample.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.intConfig
import com.ironsource.aura.airconkt.source.FireBaseConfigSource

object CoolKtConfig : FeatureRemoteConfig {
    override val source = FireBaseConfigSource::class

    //    var someInt by IntConfig()
//            .defaultValue(20)
//
//    val someLong by LongConfig()
//            .defaultValue(99999999999999L)
//
//    val someFloat by FloatConfig()
//            .defaultValue(5f)
//
//    val someString by StringConfig()
//            .defaultValue("Default")
//
//    val someBoolean by BooleanConfig()
//            .defaultValue(false)
//
//    val someNullableString by StringConfig()
//
//    val someList by JsonConfig.create<List<String>>()
//

    val feature_enabled by intConfig {
        default = 6
        constraint {
            acceptIf { it % 2 == 0 }
            fallbackTo { it + 1 }
        }
    }
}