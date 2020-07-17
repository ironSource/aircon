package com.ironsource.aura.airconkt.sample.config

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.annotations.RemoteIntValue
import com.ironsource.aura.airconkt.config.annotations.RemoteStringValue
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.source.FireBaseConfigSource

object CoolKtConfig : FeatureRemoteConfig {
    override val source = FireBaseConfigSource::class

    var someInt by intConfig {
        default = 20
    }

    var someLong by longConfig {
        default = 999999999999999999
    }

    val someFloat by floatConfig {
        default = 0.5f
    }

    val someString by stringConfig {
        default = ""
    }

    val someBoolean by booleanConfig {
        default = true
    }

    val someList by jsonConfig<List<Int>> {
        default = listOf(1, 2)
    }

    val someNullableString by nullableStringConfig {
        default = null
    }

    val location by intEnumConfig(Location::class) {
        default = Location.TOP
    }

    val size by stringEnumConfig(Size::class) {
        default = Size.SMALL
    }

    val feature_enabled by booleanConfig {
        default = false
    }

    val someTypedInt by typedIntConfig<String> {
        adapt { "$it" }
    }

    val someLabelConfig by labelConfig {
        default = Label("Default")
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