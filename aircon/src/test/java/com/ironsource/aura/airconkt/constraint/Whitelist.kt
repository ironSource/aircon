package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.constraint.whitelist
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object Whitelist : Spek({
    class InWhitelistConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 1
            whitelist = listOf(1, 3, 5)
        }
    }

    class NotInWhitelistConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 1
            whitelist = listOf(1, 3)
        }
    }

    val inWhiteListConfig = InWhitelistConfig()
    val notInWhiteListConfig = NotInWhitelistConfig()

    beforeGroup {
        initSdk(mutableMapOf(
                "someInt" to 5
        ))
    }

    describe("Remote value not in whitelist should fallback to default") {
        it("intConfig") {
            assertEquals(1, notInWhiteListConfig.someInt)
        }
    }

    describe("Remote value in whitelist should be valid") {
        it("intConfig") {
            assertEquals(5, inWhiteListConfig.someInt)
        }
    }
})