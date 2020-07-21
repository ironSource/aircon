package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

object GetCache : Spek(airConTest {

    describe("Cached config should calculate value only once") {

        class CacheConfig : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig { }
            val someLong by longConfig { }
            val someFloat by floatConfig { }
            val someString by stringConfig { }
            val someBoolean by booleanConfig { }
            val someTyped by typedConfig<Label> {}
        }

        val cacheConfig = CacheConfig()

        beforeGroup {
            recalcMap()
        }

        it("Should return original value after map is updated - intConfig") {
            val value = cacheConfig.someInt
            recalcMap()
            assertEquals(value, cacheConfig.someInt)
        }

        it("Should return original value after map is updated - longConfig") {
            val value = cacheConfig.someLong
            recalcMap()
            assertEquals(value, cacheConfig.someLong)
        }

        it("Should return original value after map is updated - floatConfig") {
            val value = cacheConfig.someFloat
            recalcMap()
            assertEquals(value, cacheConfig.someFloat)
        }

        it("Should return original value after map is updated - stringConfig") {
            val value = cacheConfig.someString
            recalcMap()
            assertEquals(value, cacheConfig.someString)
        }

        it("Should return original value after map is updated - typedConfig") {
            val value = cacheConfig.someTyped
            recalcMap()
            assertEquals(value, cacheConfig.someTyped)
        }
    }

    describe("Non cached config should resolve value on every field read") {

        class NoCacheConfig : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig { cacheValue = false }
            val someLong by longConfig { cacheValue = false }
            val someFloat by floatConfig { cacheValue = false }
            val someString by stringConfig { cacheValue = false }
            val someBoolean by booleanConfig { cacheValue = false }
            val someTyped by typedConfig<Label> { cacheValue = false }
        }

        val nonCacheConfig = NoCacheConfig()

        beforeGroup {
            recalcMap()
        }

        it("Should return updated value after map is updated - intConfig") {
            val value = nonCacheConfig.someInt
            recalcMap()
            assertNotEquals(value, nonCacheConfig.someInt)
        }

        it("Should return updated value after map is updated - longConfig") {
            val value = nonCacheConfig.someLong
            recalcMap()
            assertNotEquals(value, nonCacheConfig.someLong)
        }

        it("Should return updated value after map is updated - floatConfig") {
            val value = nonCacheConfig.someFloat
            recalcMap()
            assertNotEquals(value, nonCacheConfig.someFloat)
        }

        it("Should return updated value after map is updated - stringConfig") {
            val value = nonCacheConfig.someString
            recalcMap()
            assertNotEquals(value, nonCacheConfig.someString)
        }

        it("Should return updated value after map is updated - typedConfig") {
            val value = nonCacheConfig.someTyped
            recalcMap()
            assertNotEquals(value, nonCacheConfig.someTyped)
        }
    }
})

private fun recalcMap() {
    val random = Random()

    withRemoteMap(
            "someInt" to random.nextInt(),
            "someLong" to random.nextLong(),
            "someFloat" to random.nextFloat(),
            "someString" to "${random.nextLong()}",
            "someBoolean" to random.nextBoolean(),
            "someTyped" to Label(UUID.randomUUID().toString()))
}
