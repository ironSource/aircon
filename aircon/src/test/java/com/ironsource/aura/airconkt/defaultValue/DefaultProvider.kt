package com.ironsource.aura.airconkt.defaultValue

import android.graphics.Color
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.config.type.*
import com.ironsource.aura.airconkt.utils.ColorInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

object DefaultProvider : Spek(airConTest {

    class Config : FeatureRemoteConfig by mapConfig() {
        val someInt by intConfig {
            default { 0 }
        }

        val someIntWithCache by intConfig {
            default { Random().nextInt() }
        }

        val someIntWithoutCache by intConfig {
            default(cache = false) { Random().nextInt() }
        }

        val someLong by longConfig {
            default { 0 }
        }
        val someFloat by floatConfig {
            default { 0f }
        }
        val someString by stringConfig {
            default { "" }
        }
        val someNullableString by nullableStringConfig {
            default { null }
        }
        val someBoolean by booleanConfig {
            default { false }
        }
        val someColor by colorConfig {
            default { ColorInt(Color.WHITE) }
        }
    }

    val config = Config()

    describe("Fallback to default provider resolved value when no remote value configured") {

        it("Should return default - intConfig") {
            assertEquals(0, config.someInt)
        }
        it("Should return default - longConfig") {
            assertEquals(0, config.someLong)
        }
        it("Should return default - floatConfig") {
            assertEquals(0f, config.someFloat)
        }
        it("Should return default - Should return default - stringConfig") {
            assertEquals("", config.someString)
        }
        it("Should return default - nullableStringConfig") {
            assertEquals(null, config.someNullableString)
        }
        it("Should return default - booleanConfig") {
            assertEquals(false, config.someBoolean)
        }
        it("Should return default - colorConfig") {
            assertEquals(ColorInt(Color.WHITE), config.someColor)
        }
    }

    describe("Cache flag should control number of calls to default provider") {

        it("Config with cache - default provider should only be called once") {
            assertEquals(config.someIntWithCache, config.someIntWithCache)
        }

        it("Config without cache - default provider should be called every time field getter called") {
            assertNotEquals(config.someIntWithoutCache, config.someIntWithoutCache)
        }
    }
})