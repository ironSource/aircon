package com.ironsource.aura.airconkt.source

internal class StubConfigSource : ConfigSource {

    override fun getInteger(key: String) = null

    override fun putInteger(key: String,
                            value: Int?) {
        // No-op
    }

    override fun getLong(key: String) = null

    override fun putLong(key: String,
                         value: Long?) {
        // No-op
    }

    override fun getFloat(key: String) = null

    override fun putFloat(key: String,
                          value: Float?) {
        // No-op
    }

    override fun getBoolean(key: String) = null

    override fun putBoolean(key: String,
                            value: Boolean?) {
        // No-op
    }

    override fun getString(key: String) = null

    override fun putString(key: String,
                           value: String?) {
        // No-op
    }

    override fun getAny(key: String) = null

    override fun putAny(key: String,
                        value: Any?) {
        // No-op
    }
}