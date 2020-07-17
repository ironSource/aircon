package com.ironsource.aura.airconkt.sample.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.annotations.RemoteIntValue
import com.ironsource.aura.airconkt.config.annotations.RemoteStringValue
import com.ironsource.aura.airconkt.config.type.intConfig
import com.ironsource.aura.airconkt.config.type.intEnumConfig
import com.ironsource.aura.airconkt.config.type.stringEnumConfig
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

    val location by intEnumConfig(Location::class) {
        default = Location.TOP
    }

    val size by stringEnumConfig(Size::class) {
        default = Size.SMALL
    }

    val feature_enabled by intConfig {
        default = 6
        constraint {
            acceptIf { it % 2 == 0 }
            fallbackTo { it + 1 }
        }
    }
}

enum class Location {
    @RemoteIntValue(0)
    TOP,

    @RemoteIntValue(1)
    BOTTOM
}

enum class Size {
    @RemoteStringValue("s")
    SMALL,

    @RemoteStringValue("l")
    LARGE
}