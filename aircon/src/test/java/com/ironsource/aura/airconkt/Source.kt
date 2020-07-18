package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.common.MapSource
import com.ironsource.aura.airconkt.common.MapSource2
import com.ironsource.aura.airconkt.common.initSdk
import com.ironsource.aura.airconkt.config.type.intConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object Source : Spek({
    class Config : FeatureRemoteConfig {
        override val source = MapSource::class

        val classSourceInt by intConfig {
            key = "someInt"
        }

        val specificSourceInt by intConfig {
            key = "someInt"
            source = MapSource2::class
        }
    }

    val config = Config()

    val someIntMap1Value = 1
    val someIntMap2Value = 2
    beforeGroup {
        initSdk(mutableMapOf("someInt" to someIntMap1Value),
                mutableMapOf("someInt" to someIntMap2Value))
    }

    describe("Use correct source") {
        it("Should inherit source from class") {
            assertEquals(someIntMap1Value, config.classSourceInt)
        }
        it("Should use user provided source") {
            assertEquals(someIntMap2Value, config.specificSourceInt)
        }
    }
})
