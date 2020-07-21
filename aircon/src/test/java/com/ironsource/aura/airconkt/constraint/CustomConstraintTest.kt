package com.ironsource.aura.airconkt.constraint

import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.type.intConfig
import com.ironsource.aura.airconkt.config.type.typedConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object CustomConstraintTest : Spek(airConTest {

    describe("Custom constraint should control acceptable remote values") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val someEvenOnlyInt by intConfig {
                default = 2
                cacheValue = false
                constraint {
                    acceptIf { it % 2 == 0 }
                    fallbackTo { it + 1 }
                }
            }

            val someNotEmptyLabel by typedConfig<Label> {
                cacheValue = false
                constraint {
                    acceptIf { (it as Label).value.isNotEmpty() }
                    fallbackTo { Label("fallback") }
                }
            }
        }

        val config = Config()

        it("Should return remote value when valid by constraint") {
            withRemoteMap("someEvenOnlyInt" to 2)

            assertEquals(2, config.someEvenOnlyInt)
        }

        it("Should return remote value when valid by constraint - typedConfig") {
            withRemoteMap("someNotEmptyLabel" to Label("hello"))

            assertEquals(Label("hello"), config.someNotEmptyLabel)
        }

        it("Should return defined fallback value when remote value not valid by constraint") {
            withRemoteMap("someEvenOnlyInt" to 3)

            assertEquals(4, config.someEvenOnlyInt)
        }

        it("Should return defined fallback value when remote value not valid by constraint - typedConfig") {
            withRemoteMap("someNotEmptyLabel" to Label(""))

            assertEquals(Label("fallback"), config.someNotEmptyLabel)
        }
    }
})