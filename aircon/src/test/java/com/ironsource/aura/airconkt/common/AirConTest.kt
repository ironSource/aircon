package com.ironsource.aura.airconkt.common

import com.google.gson.Gson
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.FeatureRemoteConfig
import com.ironsource.aura.airconkt.converter.gson.GsonConverter
import org.spekframework.spek2.dsl.Root

fun airConTest(block: Root.() -> Unit): Root.() -> Unit {
    return {

        beforeGroup {
            AirConKt.init(mockContext()) {
                logging {
                    logger = ConsoleLogger()
                }
                jsonConverter = GsonConverter(Gson())
            }

            withRemoteMap()
        }

        block()
    }
}

fun mapConfig() = object : FeatureRemoteConfig {
    override val source = MapSource::class
}

fun map2Config() = object : FeatureRemoteConfig {
    override val source = MapSource2::class
}

fun withRemoteMap(vararg pairs: Pair<String, Any?>) {
    AirConKt.configSourceRepository.addSource(MapSource(mutableMapOf(*pairs)))
}

fun withRemoteMap2(vararg pairs: Pair<String, Any?>) {
    AirConKt.configSourceRepository.addSource(MapSource2(mutableMapOf(*pairs)))
}