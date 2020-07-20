package com.ironsource.aura.airconkt.defaultValue

import android.graphics.Color
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.parseColor
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.utils.ColorInt
import io.mockk.every
import io.mockk.mockkStatic
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object DefaultResTest : Spek(airConTest {

    describe("Fallback to resource default when no remote value configured") {

        class Config : FeatureRemoteConfig by mapConfig() {

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
            val someBoolean by booleanConfig {
                defaultRes = 0
            }
            val someColor by colorConfig {
                defaultRes = 0
            }
        }

        val config = Config()

        it("Should return default resource - intConfig") {
            assertEquals(0, config.someInt)
        }

        it("Should return default resource - longConfig") {
            assertEquals(0, config.someLong)
        }

        it("Should return default resource - floatConfig") {
            assertEquals(0f, config.someFloat)
        }

        it("Should return default resource - stringConfig") {
            assertEquals("", config.someString)
        }

        it("Should return default resource - booleanConfig") {
            assertEquals(false, config.someBoolean)
        }

        it("Should return default resource - colorConfig") {
            mockkStatic(Color::class)
            every { Color.parseColor(any()) } answers { parseColor(firstArg()) }

            assertEquals(ColorInt(Color.WHITE), config.someColor)
        }
    }
})
