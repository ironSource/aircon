package com.ironsource.aura.airconkt.defaultValue

import android.graphics.Color
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.config.type.util.ColorInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object DefaultValueTest : Spek(airConTest {

    describe("Fallback to default when no remote value configured") {

        class Config : FeatureRemoteConfig by mapConfig() {
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
                default = ColorInt(
                        Color.WHITE)
            }
            val someTyped by typedConfig<Label> {
                default = Label("default")
            }
        }

        val config = Config()

        it("Should return default - intConfig") {
            assertEquals(0, config.someInt)
        }

        it("Should return default - longConfig") {
            assertEquals(0, config.someLong)
        }

        it("Should return default - floatConfig") {
            assertEquals(0f, config.someFloat)
        }

        it("Should return default - stringConfig") {
            assertEquals("", config.someString)
        }

        it("Should return default - nullableStringConfig") {
            assertEquals(null, config.someNullableString)
        }

        it("Should return default - booleanConfig") {
            assertEquals(false, config.someBoolean)
        }

        it("Should return default - colorConfig") {
            assertEquals(ColorInt(
                    Color.WHITE), config.someColor)
        }

        it("Should return default - typedConfig") {
            assertEquals(Label("default"), config.someTyped)
        }

        it("Should return default - typedConfig with invalid type configured") {
            withRemoteMap("someTyped" to "Wrong")

            assertEquals(Label("default"), config.someTyped)
        }
    }
})
