package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.source.ConfigSource

fun <Raw, Actual, Adapted> Config<Raw, Actual>.adapted(adapter: (Actual) -> Adapted): Config<Raw, Adapted> {
    return object : AbstractConfig<Config<Raw, Adapted>, Raw, Adapted>() {

        override fun getRawValue(source: ConfigSource, key: String) = this@adapted.getRawValue(source, key)

        override fun adapt(value: Raw) = adapter(this@adapted.adapt(value))

        override fun isValid(value: Raw) = this@adapted.isValid(value)
    }
}

// TODO renamed since it clashes with the adapted function, need to rethink
fun <Raw, Actual, Adapted> MutableConfig<Raw, Actual>.adaptedMutable(adapter: (Actual) -> Adapted, serializer: (Adapted) -> Raw): MutableConfig<Raw, Adapted> {
    return object : AbstractMutableConfig<MutableConfig<Raw, Adapted>, Raw, Adapted>() {

        override fun getRawValue(source: ConfigSource, key: String) = this@adaptedMutable.getRawValue(source, key)

        override fun setRawValue(source: ConfigSource, key: String, value: Raw) {
            this@adaptedMutable.setRawValue(source, key, value)
        }

        override fun asRaw(value: Adapted) = serializer(value)

        override fun adapt(value: Raw) = adapter(this@adaptedMutable.adapt(value))

        override fun isValid(value: Raw) = this@adaptedMutable.isValid(value)
    }
}