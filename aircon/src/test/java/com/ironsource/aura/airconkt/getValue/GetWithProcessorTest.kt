package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object GetWithProcessorTest : Spek(airConTest {

    describe("Config with processor should process remote value") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig {
                default = 1
                process { it + 1 }
            }
            val someLong by longConfig {
                default = 1
                process { it + 1 }
            }
            val someFloat by floatConfig {
                default = 1f
                process { it + 1 }
            }
            val someString by stringConfig {
                default = ""
                process { "remote" + 1 }
            }
            val someStringSet by stringSetConfig {
                default = setOf()
                process { HashSet<String>(it).apply { add("remote2") } }
            }
            val someBoolean by booleanConfig {
                default = false
                process { !it }
            }
            val someTyped by typedConfig<Label> {
                default = Label("default")
                process { Label(it.value + "Processed") }
            }
        }

        val config = Config()

        beforeGroup {
            withRemoteMap(
                    "someInt" to 1,
                    "someLong" to 1L,
                    "someFloat" to 1f,
                    "someString" to "remote",
                    "someStringSet" to setOf("remote"),
                    "someBoolean" to true,
                    "someTyped" to Label("remote")
            )
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

        it("Should return processed remote value - stringSetConfig") {
            assertEquals(setOf("remote", "remote2"), config.someStringSet)
        }
        it("Should return processed remote value - booleanConfig") {
            assertEquals(false, config.someBoolean)
        }

        it("Should return processed remote value - typedConfig") {
            assertEquals(Label("remoteProcessed"), config.someTyped)
        }
    }
})
