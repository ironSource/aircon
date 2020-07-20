package com.ironsource.aura.airconkt.configPropertyApi

import android.graphics.Color
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.asConfigProperty
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.utils.ColorInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object ConfigPropertyApiGetRawValueTest : Spek(airConTest {

    describe("Delegate getRawValue should return raw configured value") {

        class Config : FeatureRemoteConfig by mapConfig() {

            val someInt by intConfig {
                process { it + 1 }
            }
            val someLong by longConfig {
                process { it + 1 }
            }
            val someFloat by floatConfig {
                process { it + 1 }
            }
            val someString by stringConfig {
                process { "remote" + 1 }
            }
            val someBoolean by booleanConfig {
                process { !it }
            }
            val someColor by colorConfig {
                default = ColorInt(Color.WHITE)
            }
        }

        val config = Config()

        beforeGroup {
            withRemoteMap(
                    "someInt" to 1,
                    "someLong" to 1L,
                    "someFloat" to 1f,
                    "someString" to "remote",
                    "someBoolean" to true,
                    "someColor" to "#000000")
        }

        it("Should return raw configured value - intConfig") {
            assertEquals(1, Config::someInt.asConfigProperty(config).getIntRawValue())
        }

        it("Should return raw configured value - longConfig") {
            assertEquals(1L, Config::someLong.asConfigProperty(config).getLongRawValue())
        }

        it("Should return raw configured value - floatConfig") {
            assertEquals(1f, Config::someFloat.asConfigProperty(config).getFloatRawValue())
        }

        it("Should return raw configured value - stringConfig") {
            assertEquals("remote", Config::someString.asConfigProperty(config).getStringRawValue())
        }

        it("Should return raw configured value - booleanConfig") {
            assertEquals(true, Config::someBoolean.asConfigProperty(config).getBooleanRawValue())
        }

        it("cShould return raw configured value - colorConfig") {
            assertEquals("#000000", Config::someColor.asConfigProperty(config).getStringRawValue())
        }
    }
})
