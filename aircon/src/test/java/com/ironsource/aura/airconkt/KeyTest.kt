package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object KeyTest : Spek(airConTest {

    describe("Usage of correct config key") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig {
            }

            val someIntWithKey by intConfig {
                key = "someInt"
            }
        }

        val config = Config()

        beforeGroup {
            withRemoteMap("someInt" to 0)
        }

        it("Should use key by property name when key not set") {
            assertEquals(0, config.someInt)
        }

        it("Should use user provided key when set") {
            assertEquals(0, config.someIntWithKey)
        }
    }
})
