package com.ironsource.aura.airconkt.setValue

import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.config.type.typedStringConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SetWithSerializerTest : Spek(airConTest {

    describe("Simple config field set should return set value") {

        class Config : FeatureRemoteConfig by mapConfig() {
            var someLabel by typedStringConfig<Label> {
                adapt { Label(it) }
                serialize { it.value }
            }
        }

        val config = Config()

        it("Should return serialized set value") {
            config.someLabel = Label("test")
            assertEquals(Label("test"), config.someLabel)
            config.someLabel = Label("test2")
            assertEquals(Label("test2"), config.someLabel)
        }
    }
})
