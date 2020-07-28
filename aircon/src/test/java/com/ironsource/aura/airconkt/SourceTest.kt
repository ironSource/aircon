package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.common.*
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SourceTest : Spek(airConTest {

    describe("Usage of correct config source") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val classSourceInt by intConfig {
                key = "someInt"
                default = 1
            }

            val specificSourceInt by intConfig {
                key = "someInt"
                source = MapSource2::class
                default = 1
            }
        }

        val config = Config()

        it("Should inherit source from class when no source set for config") {
            withRemoteMap("someInt" to 1)

            assertEquals(1, config.classSourceInt)
        }

        it("Should use source set in config and ignore class source") {
            withRemoteMap2("someInt" to 2)

            assertEquals(2, config.specificSourceInt)
        }
    }
})
