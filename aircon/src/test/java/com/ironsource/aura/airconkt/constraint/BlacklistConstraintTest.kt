package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.constraint.blacklist
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object BlacklistConstraintTest : Spek(airConTest {

    describe("Blacklist should control acceptable remote values") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig {
                default = 2
                cacheValue=false
                blacklist = listOf(1, 3, 5)
            }
        }

        val config = Config()

        it("Should return remote value when not in blacklist") {
            withRemoteMap("someInt" to 4)

            assertEquals(4, config.someInt)
        }

        it("Should return default value when remote value in blacklist") {
            withRemoteMap("someInt" to 3)

            assertEquals(2, config.someInt)
        }
    }
})