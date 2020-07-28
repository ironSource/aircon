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
            val someInt by intConfig { default = 1 }
            val someLong by longConfig { default = 1 }
            val someFloat by floatConfig { default = 1f }
            val someString by stringConfig { default = "" }
            val someStringSet by stringSetConfig { default = setOf() }
            val someBoolean by booleanConfig { default = false }
            val someTyped by typedConfig<Label> { default = Label("default") }
            var someLabel by typedStringConfig<Label> {
                default = Label("default")
                adapt {
                    get { Label(it) }
                    set { it.value }
                }
            }
            val someUrl by urlConfig { default = "" }
        }

        val config = Config()

        beforeGroup {
            withRemoteMap(
                    "someInt" to 1,
                    "someLong" to 1L,
                    "someFloat" to 1f,
                    "someString" to "remote",
                    "someStringSet" to setOf("remote"),
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

        it("Should return remote value - stringSetConfig") {
            assertEquals(setOf("remote"), config.someStringSet)
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
