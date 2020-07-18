package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object BasicGet : Spek({
    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {}
        val someLong by longConfig {}
        val someFloat by floatConfig {}
        val someString by stringConfig {}
        val someBoolean by booleanConfig {}
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

    describe("Basic get remote value") {
        it("intConfig") {
            assertEquals(1, config.someInt)
        }
        it("longConfig") {
            assertEquals(1L, config.someLong)
        }
        it("floatConfig") {
            assertEquals(1f, config.someFloat)
        }
        it("stringConfig") {
            assertEquals("remote", config.someString)
        }
        it("booleanConfig") {
            assertEquals(true, config.someBoolean)
        }
    }
})
