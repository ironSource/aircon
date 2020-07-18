package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object Custom : Spek({

    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someOddOnlyInt by intConfig {
            default = 1
            constraint {
                acceptIf { it % 2 == 1 }
                fallbackTo { it + 1 }
            }
        }

        val someEvenOnlyInt by intConfig {
            default = 2
            constraint {
                acceptIf { it % 2 == 0 }
                fallbackTo { it + 1 }
            }
        }
    }

    val config = Config()

    beforeGroup {
        initSdk(mutableMapOf(
                "someOddOnlyInt" to 5,
                "someEvenOnlyInt" to 5
        ))
    }

    describe("Remote custom constraint value should be valid") {
        it("intConfig") {
            assertEquals(5, config.someOddOnlyInt)
        }
    }

    describe("Remote custom constraint invalid value should fallback to defined fallback") {
        it("intConfig") {
            assertEquals(6, config.someEvenOnlyInt)
        }
    }
})