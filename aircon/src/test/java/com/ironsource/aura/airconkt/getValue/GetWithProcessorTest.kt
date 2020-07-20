package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object GetWithProcessorTest : Spek(airConTest {

    describe("Config with processor should process remote value") {

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
        }

        val config = Config()

        beforeGroup {
            withRemoteMap(
                    "someInt" to 1,
                    "someLong" to 1L,
                    "someFloat" to 1f,
                    "someString" to "remote",
                    "someBoolean" to true)
        }

        it("Should return processed remote value - intConfig") {
            assertEquals(2, config.someInt)
        }

        it("Should return processed remote value - longConfig") {
            assertEquals(2L, config.someLong)
        }

        it("Should return processed remote value - floatConfig") {
            assertEquals(2f, config.someFloat)
        }

        it("Should return processed remote value - stringConfig") {
            assertEquals("remote1", config.someString)
        }

        it("Should return processed remote value - booleanConfig") {
            assertEquals(false, config.someBoolean)
        }
    }
})
