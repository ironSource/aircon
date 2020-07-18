package com.ironsource.aura.airconkt.configPropertyApi

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.asConfigProperty
import com.ironsource.aura.airconkt.config.type.intConfig
import com.ironsource.aura.airconkt.config.type.longConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object IsConfiguredDelegateApi : Spek({

    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val someInt by intConfig {
            process { it + 1 }
        }
        val someLong by longConfig {
            process { it + 1 }
        }
    }

    val config = Config()

    beforeGroup {
        initSdk(mutableMapOf(
                "someInt" to 1
        ))
    }

    describe("Delegate isConfigured should return true for configured values") {
        it("intConfig") {
            assertEquals(true, Config::someInt.asConfigProperty(config).isConfigured())
        }
        it("longConfig") {
            assertEquals(false, Config::someLong.asConfigProperty(config).isConfigured())
        }
    }
})
