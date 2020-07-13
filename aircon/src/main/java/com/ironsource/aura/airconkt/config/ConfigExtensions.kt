package com.ironsource.aura.airconkt.config

import com.ironsource.aura.airconkt.source.ConfigSource

fun <Raw, Actual> Config<Raw, Actual>.validated(validator: (Raw) -> Boolean): Config<Raw, Actual> {
    return object : Config<Raw, Actual> by this {
        override fun isValid(value: Raw): Boolean {
            return this@validated.isValid(value) && validator(value)
        }
    }
}

fun <Raw, Actual, Adapted> Config<Raw, Actual>.adapted(adapter: (Actual) -> Adapted): Config<Raw, Adapted> {
    return object : AbstractConfig<Raw, Adapted>() {

        override fun getRawValue(source: ConfigSource, key: String) = this@adapted.getRawValue(source, key)

        override fun adapt(value: Raw) = adapter(this@adapted.adapt(value))

        override fun isValid(value: Raw) = this@adapted.isValid(value)
    }
}