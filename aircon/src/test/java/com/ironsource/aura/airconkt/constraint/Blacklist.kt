package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.constraint.blacklist
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object Blacklist : Spek({

    class InBlacklistConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 1
            blacklist = listOf(1, 3, 5)
        }
    }

    class NotInBlacklistConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 1
            blacklist = listOf(1, 3)
        }
    }

    val inBlacklistConfig = InBlacklistConfig()
    val notInBlacklistConfig = NotInBlacklistConfig()

    beforeGroup {
        initSdk(mutableMapOf(
                "someInt" to 5
        ))
    }

    describe("Remote value not in blacklist should be valid") {
        it("intConfig") {
            assertEquals(5, notInBlacklistConfig.someInt)
        }
    }

    describe("Remote value in blacklist should fallback to default") {
        it("intConfig") {
            assertEquals(1, inBlacklistConfig.someInt)
        }
    }
})