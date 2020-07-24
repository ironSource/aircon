package com.ironsource.aura.airconkt.getValue

import android.webkit.URLUtil
import com.ironsource.aura.airconkt.common.Label
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.config.type.*
import io.mockk.every
import io.mockk.mockkStatic
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SimpleGetTest : Spek(airConTest {

    describe("Simple config field get should return remote value") {

        class Config : FeatureRemoteConfig by mapConfig() {
            val someInt by intConfig {}
            val someLong by longConfig {}
            val someFloat by floatConfig {}
            val someString by stringConfig {}
            val someBoolean by booleanConfig {}
            val someTyped by typedConfig<Label> {}
            var someLabel by typedStringConfig<Label> {
                adapt {
                    get { Label(it) }
                    set { it.value }
                }
            }
            val someUrl by urlConfig { }
        }

        val config = Config()

        beforeGroup {
            withRemoteMap(
                    "someInt" to 1,
                    "someLong" to 1L,
                    "someFloat" to 1f,
                    "someString" to "remote",
                    "someBoolean" to true,
                    "someTyped" to Label(),
                    "someLabel" to "remote",
                    "someUrl" to "www.google.com"
            )
        }

        it("Should return remote value - intConfig") {
            assertEquals(1, config.someInt)
        }

        it("Should return remote value - longConfig") {
            assertEquals(1L, config.someLong)
        }

        it("Should return remote value - floatConfig") {
            assertEquals(1f, config.someFloat)
        }

        it("Should return remote value - stringConfig") {
            assertEquals("remote", config.someString)
        }

        it("Should return remote value - booleanConfig") {
            assertEquals(true, config.someBoolean)
        }

        it("Should return remote value - typedConfig") {
            assertEquals(Label(), config.someTyped)
        }

        it("Should return remote value - typedStringConfig with adapter") {
            assertEquals(Label("remote"), config.someLabel)
        }

        it("Should return remote value - urlConfig") {
            mockkStatic(URLUtil::class)
            every { URLUtil.isValidUrl(any()) } returns true
            assertEquals("www.google.com", config.someUrl)
        }
    }
})
