package com.ironsource.aura.airconkt.defaultValue

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

object DefaultProviderCache : Spek({

    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someIntWithCache by intConfig {
            key = "someInt"
            default { Random().nextInt() }
        }

        val someIntWithoutCache by intConfig {
            key = "someInt"
            default(cache = false) { Random().nextInt() }
        }
    }

    val config = Config()

    beforeGroup {
        initSdk(mutableMapOf())
    }

    describe("Cache flag should control number of calls to default provider") {
        it("Config with cache - default provider should only be called once") {
            assertEquals(config.someIntWithCache, config.someIntWithCache)
        }

        it("Config without cache - default provider should be called everytime value is read") {
            assertNotEquals(config.someIntWithoutCache, config.someIntWithoutCache)
        }
    }
})
