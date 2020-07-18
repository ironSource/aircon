package com.ironsource.aura.airconkt.defaultValue

import android.graphics.Color
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.utils.ColorInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object DefaultValue : Spek({
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

    describe("Fallback to default") {
        it("intConfig") {
            assertEquals(0, config.someInt)
        }
        it("longConfig") {
            assertEquals(0, config.someLong)
        }
        it("floatConfig") {
            assertEquals(0f, config.someFloat)
        }
        it("stringConfig") {
            assertEquals("", config.someString)
        }
        it("nullableStringConfig") {
            assertEquals(null, config.someNullableString)
        }
        it("booleanConfig") {
            assertEquals(false, config.someBoolean)
        }
        it("colorConfig") {
            assertEquals(ColorInt(Color.WHITE), config.someColor)
        }
    }
})
