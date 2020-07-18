package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object Key : Spek({
    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
        }

        val someIntWithKey by intConfig {
            key = "someInt"
        }
    }

    val config = Config()

    val someIntValue = 10
    beforeGroup {
        initSdk(mutableMapOf("someInt" to someIntValue))
    }

    describe("Use correct key") {
        it("Should use key by property name") {
            assertEquals(someIntValue, config.someInt)
        }
        it("Should use user provided key") {
            assertEquals(someIntValue, config.someIntWithKey)
        }
    }
})
