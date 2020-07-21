package com.ironsource.aura.airconkt.setValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SimpleSetTest : Spek(airConTest {

    describe("Simple config field set should return set value") {

        class Config : FeatureRemoteConfig by mapConfig() {
            var someInt by intConfig {}
            var someLong by longConfig {}
            var someFloat by floatConfig {}
            var someString by stringConfig {}
            var someBoolean by booleanConfig {}
            var someTyped by typedConfig<Label> {}
        }

        val config = Config()

        it("Should return set value - intConfig") {
            config.someInt = 1
            assertEquals(1, config.someInt)
            config.someInt = 2
            assertEquals(2, config.someInt)
        }

        it("Should return set value - longConfig") {
            config.someLong = 1
            assertEquals(1L, config.someLong)
            config.someLong = 2
            assertEquals(2L, config.someLong)
        }

        it("Should return set value - floatConfig") {
            config.someFloat = 1f
            assertEquals(1f, config.someFloat)
            config.someFloat = 2f
            assertEquals(2f, config.someFloat)
        }

        it("Should return set value - stringConfig") {
            config.someString = ""
            assertEquals("", config.someString)
            config.someString = "new"
            assertEquals("new", config.someString)
        }

        it("Should return set value - booleanConfig") {
            config.someBoolean = true
            assertEquals(true, config.someBoolean)
            config.someBoolean = false
            assertEquals(false, config.someBoolean)
        }

        it("Should return set value - anyConfig") {
            config.someTyped = Label()
            assertEquals(Label(), config.someTyped)

            config.someTyped = Label("new")
            assertEquals(Label("new"), config.someTyped)
        }
    }
})
