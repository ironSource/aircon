package com.ironsource.aura.airconkt.customConfigs

import com.ironsource.aura.airconkt.FeatureRemoteConfig
import com.ironsource.aura.airconkt.common.airConTest
import com.ironsource.aura.airconkt.common.mapConfig
import com.ironsource.aura.airconkt.common.withRemoteMap
import com.ironsource.aura.airconkt.config.annotations.RemoteIntValue
import com.ironsource.aura.airconkt.config.annotations.RemoteStringValue
import com.ironsource.aura.airconkt.config.type.intEnumConfig
import com.ironsource.aura.airconkt.config.type.stringEnumConfig
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

enum class Location {
    @RemoteIntValue(0)
    TOP,

    @RemoteIntValue(1)
    BOTTOM
}

enum class Size {
    @RemoteStringValue("s")
    SMALL,

    @RemoteStringValue("l")
    LARGE
}

object EnumConfigTest : Spek(airConTest {

    class Config : FeatureRemoteConfig by mapConfig() {
        var someIntEnum by intEnumConfig(Location::class) {
            cacheValue = false
        }

        var someStringEnum by stringEnumConfig(Size::class) {
            cacheValue = false
        }
    }

    val config = Config()

    describe("Enum config field get should return remote value") {

        beforeGroup {
            withRemoteMap(
                    "someIntEnum" to 1,
                    "someStringEnum" to "l"
            )
        }

        it("Should return remote value - intEnum") {
            assertEquals(Location.BOTTOM, config.someIntEnum)
        }

        it("Should return remote value - stringEnum") {
            assertEquals(Size.LARGE, config.someStringEnum)
        }
    }

    describe("Enum config field get should return set value") {

        it("Should return set value - intEnum") {
            config.someIntEnum = Location.TOP
            assertEquals(Location.TOP, config.someIntEnum)
        }

        it("Should return set value - stringEnum") {
            config.someStringEnum = Size.SMALL
            assertEquals(Size.SMALL, config.someStringEnum)
        }
    }
})
