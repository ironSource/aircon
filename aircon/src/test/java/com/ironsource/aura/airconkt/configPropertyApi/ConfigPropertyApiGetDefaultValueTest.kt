//package com.ironsource.aura.airconkt.configPropertyApi
//
//import android.graphics.Color
//import com.ironsource.aura.airconkt.common.airConTest
//import com.ironsource.aura.airconkt.common.mapConfig
//import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
//import com.ironsource.aura.airconkt.config.asConfigProperty
//import com.ironsource.aura.airconkt.config.type.*
//import com.ironsource.aura.airconkt.config.type.util.ColorInt
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.specification.describe
//import kotlin.test.assertEquals
//
//object ConfigPropertyApiGetDefaultValueTest : Spek(airConTest {
//
//    describe("Delegate getDefaultValue should return defined default") {
//
//        class Config : FeatureRemoteConfig by mapConfig() {
//            val someInt by intConfig {
//                default = 0
//            }
//            val someLong by longConfig {
//                default = 0
//            }
//            val someFloat by floatConfig {
//                default = 0f
//            }
//            val someString by stringConfig {
//                default = ""
//            }
//            val someStringSet by stringSetConfig {
//                default = setOf("")
//            }
//            val someNullableString by nullableStringConfig {
//                default = null
//            }
//            val someBoolean by booleanConfig {
//                default = false
//            }
//            val someColor by colorConfig {
//                default = ColorInt(
//                        Color.WHITE)
//            }
//        }
//
//        val config = Config()
//
//        it("Should return default - intConfig") {
//            assertEquals(0, Config::someInt.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - longConfig") {
//            assertEquals(0, Config::someLong.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - floatConfig") {
//            assertEquals(0f, Config::someFloat.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - stringConfig") {
//            assertEquals("", Config::someString.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - stringSetConfig") {
//            assertEquals(setOf(""),
//                    Config::someStringSet.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - nullableStringConfig") {
//            assertEquals(null,
//                    Config::someNullableString.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - booleanConfig") {
//            assertEquals(false, Config::someBoolean.asConfigProperty(config).getDefaultValue())
//        }
//
//        it("Should return default - colorConfig") {
//            assertEquals(ColorInt(
//                    Color.WHITE), Config::someColor.asConfigProperty(config).getDefaultValue())
//        }
//    }
//})
