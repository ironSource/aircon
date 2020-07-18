package com.ironsource.aura.airconkt.defaultValue

import android.graphics.Color
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.common.parseColor
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.utils.ColorInt
import io.mockk.every
import io.mockk.mockkStatic
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object DefaultRes : Spek({
    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            defaultRes = 0
        }
        val someLong by longConfig {
            defaultRes = 0
        }
        val someFloat by floatConfig {
            defaultRes = 0
        }
        val someString by stringConfig {
            defaultRes = 0
        }
        val someNullableString by nullableStringConfig {
            defaultRes = 0
        }
        val someBoolean by booleanConfig {
            defaultRes = 0
        }
        val someColor by colorConfig {
            defaultRes = 0
        }
    }

    val config = Config()

    beforeGroup {
        initSdk(mutableMapOf())
        mockkStatic(Color::class)
        every { Color.parseColor(any()) } answers { parseColor(firstArg()) }
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
        it("booleanConfig") {
            assertEquals(false, config.someBoolean)
        }
        it("colorConfig") {
            assertEquals(ColorInt(Color.WHITE), config.someColor)
        }
    }
})
