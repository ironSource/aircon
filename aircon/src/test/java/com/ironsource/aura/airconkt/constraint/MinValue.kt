package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.constraint.FallbackPolicy
import com.ironsource.aura.airconkt.config.constraint.minValue
import com.ironsource.aura.airconkt.config.type.floatConfig
import com.ironsource.aura.airconkt.config.type.intConfig
import com.ironsource.aura.airconkt.config.type.longConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object MinValue : Spek({

    class DefaultFallbackConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 3
            minValue = 2
        }
        val someLong by longConfig {
            default = 3
            minValue = 2
        }
        val someFloat by floatConfig {
            default = 3f
            minValue = 2f
        }
    }

    class RangeFallbackConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            default = 3
            minValue(2, FallbackPolicy.RANGE)
        }
        val someLong by longConfig {
            default = 3
            minValue(2L, FallbackPolicy.RANGE)
        }
        val someFloat by floatConfig {
            default = 3f
            minValue(2f, FallbackPolicy.RANGE)
        }
    }

    class ValidConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            minValue = 1
        }
        val someLong by longConfig {
            minValue = 1
        }
        val someFloat by floatConfig {
            minValue = 1f
        }
    }

    val defaultFallbackConfig = DefaultFallbackConfig()
    val rangeFallbackConfig = RangeFallbackConfig()
    val validConfig = ValidConfig()

    beforeGroup {
        initSdk(mutableMapOf(
                "someInt" to 1,
                "someLong" to 1L,
                "someFloat" to 1f
        ))
    }

    describe("Remote value smaller than minValue should fallback to default") {
        it("intConfig") {
            assertEquals(3, defaultFallbackConfig.someInt)
        }
        it("longConfig") {
            assertEquals(3L, defaultFallbackConfig.someLong)
        }
        it("floatConfig") {
            assertEquals(3f, defaultFallbackConfig.someFloat)
        }
    }

    describe("Remote value smaller than minValue with range fallback should fall to it") {
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

    describe("Remote value equal or greater than minValue should be valid") {
        it("intConfig") {
            assertEquals(1, validConfig.someInt)
        }
        it("longConfig") {
            assertEquals(1L, validConfig.someLong)
        }
        it("floatConfig") {
            assertEquals(1f, validConfig.someFloat)
        }
    }
})