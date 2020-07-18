package com.ironsource.aura.airconkt.configPropertyApi

import android.graphics.Color
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.asConfigProperty
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.utils.ColorInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object GetDefaultValueDelegateApi : Spek({

    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 0
        }
        val someLong by longConfig {
            default = 0
        }
        val someFloat by floatConfig {
            default = 0f
        }
        val someString by stringConfig {
            default = ""
        }
        val someNullableString by nullableStringConfig {
            default = null
        }
        val someBoolean by booleanConfig {
            default = false
        }
        val someColor by colorConfig {
            default = ColorInt(Color.WHITE)
        }
    }

    val config = Config()

    beforeGroup {
        initSdk(mutableMapOf())
    }

    describe("Delegate getDefaultValue should return defined default") {
        it("intConfig") {
            assertEquals(0, Config::someInt.asConfigProperty(config).getDefaultValue())
        }
        it("longConfig") {
            assertEquals(0, Config::someLong.asConfigProperty(config).getDefaultValue())
        }
        it("floatConfig") {
            assertEquals(0f, Config::someFloat.asConfigProperty(config).getDefaultValue())
        }
        it("stringConfig") {
            assertEquals("", Config::someString.asConfigProperty(config).getDefaultValue())
        }
        it("nullableStringConfig") {
            assertEquals(null, Config::someNullableString.asConfigProperty(config).getDefaultValue())
        }
        it("booleanConfig") {
            assertEquals(false, Config::someBoolean.asConfigProperty(config).getDefaultValue())
        }
        it("colorConfig") {
            assertEquals(ColorInt(Color.WHITE), Config::someColor.asConfigProperty(config).getDefaultValue())
        }
    }
})
