package com.ironsource.aura.airconkt.getValue

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

object GetCache : Spek({
    class NonCacheConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig { cacheValue = false }
        val someLong by longConfig { cacheValue = false }
        val someFloat by floatConfig { cacheValue = false }
        val someString by stringConfig { cacheValue = false }
        val someBoolean by booleanConfig { cacheValue = false }
    }

    class CacheConfig : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig { }
        val someLong by longConfig { }
        val someFloat by floatConfig { }
        val someString by stringConfig { }
        val someBoolean by booleanConfig { }
    }

    val cacheConfig = CacheConfig()
    val nonCacheConfig = NonCacheConfig()

    beforeGroup {
        initSdk(getMap())
    }

    describe("Cached config should calculate value only once") {
        it("intConfig") {
            val value = cacheConfig.someInt
            invalidateMap()
            assertEquals(value, cacheConfig.someInt)
        }
        it("longConfig") {
            val value = cacheConfig.someLong
            invalidateMap()
            assertEquals(value, cacheConfig.someLong)
        }
        it("floatConfig") {
            val value = cacheConfig.someFloat
            invalidateMap()
            assertEquals(value, cacheConfig.someFloat)
        }
        it("stringConfig") {
            val value = cacheConfig.someString
            invalidateMap()
            assertEquals(value, cacheConfig.someString)
        }
        it("booleanConfig") {
            val value = cacheConfig.someBoolean
            invalidateMap()
            assertEquals(value, cacheConfig.someBoolean)
        }
    }

    describe("Non cached config should calculate value every time") {
        it("intConfig") {
            val value = nonCacheConfig.someInt
            invalidateMap()
            assertNotEquals(value, nonCacheConfig.someInt)
        }
        it("longConfig") {
            val value = nonCacheConfig.someLong
            invalidateMap()
            assertNotEquals(value, nonCacheConfig.someLong)
        }
        it("floatConfig") {
            val value = nonCacheConfig.someFloat
            invalidateMap()
            assertNotEquals(value, nonCacheConfig.someFloat)
        }
        it("stringConfig") {
            val value = nonCacheConfig.someString
            invalidateMap()
            assertNotEquals(value, nonCacheConfig.someString)
        }
        it("booleanConfig") {
            val value = nonCacheConfig.someBoolean
            invalidateMap()
            assertNotEquals(value, nonCacheConfig.someBoolean)
        }
    }
})

private fun invalidateMap() {
    AirConKt.configSourceRepository.addSource(MapSource(getMap()))
}

private fun getMap(): MutableMap<String, Any?> {
    val random = Random()
    return mutableMapOf(
            "someInt" to random.nextInt(),
            "someLong" to random.nextLong(),
            "someFloat" to random.nextFloat(),
            "someString" to "${random.nextLong()}",
            "someBoolean" to random.nextBoolean()
    )
}