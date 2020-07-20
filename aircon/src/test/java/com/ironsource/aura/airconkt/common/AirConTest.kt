package com.ironsource.aura.airconkt.common

import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.FeatureRemoteConfig
import org.spekframework.spek2.dsl.Root

inline fun airConTest(crossinline block: Root.() -> Unit): Root.() -> Unit {
    return {
        AirConKt.init(mockContext()) {
            logging {
                logger = ConsoleLogger()
            }
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