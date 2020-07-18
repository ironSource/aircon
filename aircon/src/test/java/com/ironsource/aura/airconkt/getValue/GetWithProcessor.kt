package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object GetWithProcessor : Spek({
    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

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
        initSdk(mutableMapOf(
                "someInt" to 1,
                "someLong" to 1L,
                "someFloat" to 1f,
                "someString" to "remote",
                "someBoolean" to true
        ))
    }

    describe("Config with processor should process remote value") {
        it("intConfig") {
            assertEquals(2, config.someInt)
        }
        it("longConfig") {
            assertEquals(2L, config.someLong)
        }
        it("floatConfig") {
            assertEquals(2f, config.someFloat)
        }
        it("stringConfig") {
            assertEquals("remote1", config.someString)
        }
        it("booleanConfig") {
            assertEquals(false, config.someBoolean)
        }
    }
})
