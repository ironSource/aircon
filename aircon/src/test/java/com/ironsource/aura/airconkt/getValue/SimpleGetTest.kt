package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SimpleGetTest : Spek(airConTest {

    describe("Simple config field get should return remote value") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig {}
            val someLong by longConfig {}
            val someFloat by floatConfig {}
            val someString by stringConfig {}
            val someBoolean by booleanConfig {}
            val someTyped by typedConfig<Label> {}
        }

        val config = Config()

        beforeGroup {
            withRemoteMap(
                    "someInt" to 1,
                    "someLong" to 1L,
                    "someFloat" to 1f,
                    "someString" to "remote",
                    "someBoolean" to true,
                    "someAny" to Label()
            )
        }

        it("Should return remote value - intConfig") {
            assertEquals(1, config.someInt)
        }

        it("Should return remote value - longConfig") {
            assertEquals(1L, config.someLong)
        }

        it("Should return remote value - floatConfig") {
            assertEquals(1f, config.someFloat)
        }

        it("Should return remote value - stringConfig") {
            assertEquals("remote", config.someString)
        }

        it("Should return remote value - booleanConfig") {
            assertEquals(true, config.someBoolean)
        }

        it("Should return remote value - anyConfig") {
            assertEquals(Label(), config.someTyped)
        }
    }
})
