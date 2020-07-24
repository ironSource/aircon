package com.ironsource.aura.airconkt.common

import com.ironsource.aura.airconkt.source.ConfigSource

open class MapSource(private val map: MutableMap<String, Any?>) : ConfigSource {

    override fun getInteger(key: String) = testAndGet<Int>(key)

    override fun putInteger(key: String,
                            value: Int?) {
        map[key] = value
    }

    override fun getLong(key: String) = testAndGet<Long>(key)

    override fun putLong(key: String,
                         value: Long?) {
        map[key] = value
    }

    override fun getFloat(key: String) = testAndGet<Float>(key)

    override fun putFloat(key: String,
                          value: Float?) {
        map[key] = value
    }

    override fun getBoolean(key: String) = testAndGet<Boolean>(key)

    override fun putBoolean(key: String,
                            value: Boolean?) {
        map[key] = value
    }

    override fun getString(key: String) = testAndGet<String>(key)

    override fun putString(key: String,
                           value: String?) {
        map[key] = value
    }

    override fun getStringSet(key: String) = testAndGet<Set<String>>(key)

    override fun putStringSet(key: String,
                              value: Set<String>?) {
        map[key] = value
    }

    override fun getAny(key: String) = testAndGet<Any>(key)

    override fun putAny(key: String,
                        value: Any?) {
        map[key] = value
    }

    private inline fun <reified T> testAndGet(key: String): T? {
        val value = map[key]
        return if (value is T) value else null
    }
}