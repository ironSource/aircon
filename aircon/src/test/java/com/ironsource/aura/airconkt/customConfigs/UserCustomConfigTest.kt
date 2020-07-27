package com.ironsource.aura.airconkt.customConfigs

import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.Config
import com.ironsource.aura.airconkt.config.ConfigPropertyFactory
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.SourceTypeResolver
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

data class Label(val value: String)

fun FeatureRemoteConfig.labelConfig(block: Config<String, Label>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.string(),
                validator = { it.isNotEmpty() },
                getterAdapter = { Label(it) },
                setterAdapter = { it.value },
                block = block
        )

object UserCustomConfigTest : Spek(airConTest {

    class Config : FeatureRemoteConfig by mapConfig() {
        var someLabel by labelConfig {
            default = Label("default")
            cached = false
        }
    }

    val config = Config()

    describe("Enum config field get") {

        it("Should return remote value when valid") {
            withRemoteMap(
                    "someLabel" to "remote"
            )

            assertEquals(Label("remote"), config.someLabel)
        }

        it("Should return default value when invalid") {
            withRemoteMap(
                    "someLabel" to ""
            )

            assertEquals(Label("default"), config.someLabel)
        }
    }

    describe("Enum config field set") {

        it("Should return set value") {
            config.someLabel = Label("override")
            assertEquals(Label("override"), config.someLabel)
        }
    }
})

