package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.constraint.FallbackPolicy
import com.ironsource.aura.airconkt.config.constraint.maxValue
import com.ironsource.aura.airconkt.config.type.floatConfig
import com.ironsource.aura.airconkt.config.type.intConfig
import com.ironsource.aura.airconkt.config.type.longConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object MaxValue : Spek({

    class DefaultFallbackConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 0
            maxValue = 2
        }
        val someLong by longConfig {
            default = 0
            maxValue = 2
        }
        val someFloat by floatConfig {
            default = 0f
            maxValue = 2f
        }
    }

    class RangeFallbackConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            maxValue(2, FallbackPolicy.RANGE)
        }
        val someLong by longConfig {
            maxValue(2L, FallbackPolicy.RANGE)
        }
        val someFloat by floatConfig {
            maxValue(2f, FallbackPolicy.RANGE)
        }
    }

    class ValidConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            maxValue = 3
        }
        val someLong by longConfig {
            maxValue = 3
        }
        val someFloat by floatConfig {
            maxValue = 3f
        }
    }

    val defaultFallbackConfig = DefaultFallbackConfig()
    val rangeFallbackConfig = RangeFallbackConfig()
    val validConfig = ValidConfig()

    beforeGroup {
        initSdk(mutableMapOf(
                "someInt" to 3,
                "someLong" to 3L,
                "someFloat" to 3f
        ))
    }

    describe("Remote value larger than maxValue should fallback to default") {
        it("intConfig") {
            assertEquals(0, defaultFallbackConfig.someInt)
        }
        it("longConfig") {
            assertEquals(0L, defaultFallbackConfig.someLong)
        }
        it("floatConfig") {
            assertEquals(0f, defaultFallbackConfig.someFloat)
        }
    }

    describe("Remote value larger than maxValue with range fallback should fall to it") {
        it("intConfig") {
            assertEquals(2, rangeFallbackConfig.someInt)
        }
        it("longConfig") {
            assertEquals(2L, rangeFallbackConfig.someLong)
        }
        it("floatConfig") {
            assertEquals(2f, rangeFallbackConfig.someFloat)
        }
    }

    describe("Remote value equal to or smaller than maxValue should be valid") {
        it("intConfig") {
            assertEquals(3, validConfig.someInt)
        }
        it("longConfig") {
            assertEquals(3L, validConfig.someLong)
        }
        it("floatConfig") {
            assertEquals(3f, validConfig.someFloat)
        }
    }
})