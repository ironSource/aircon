package com.ironsource.aura.airconkt.config

import android.content.res.Resources
import com.ironsource.aura.airconkt.source.ConfigSource

class IntConfig : SimpleResourcedConfig<Int, IntConfig>() {
    override fun resolve(resources: Resources, resource: Resource): Int {
        return resources.getInteger(resource.resId)
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getInteger(key)

    override fun setRawValue(source: ConfigSource, key: String, value: Int) {
        source.putInteger(key, value)
    }
}

class LongConfig : SimpleResourcedConfig<Long, LongConfig>() {
    override fun resolve(resources: Resources, resource: Resource): Long {
        return resources.getInteger(resource.resId).toLong()
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getLong(key)

    override fun setRawValue(source: ConfigSource, key: String, value: Long) {
        source.putLong(key, value)
    }
}

class FloatConfig : SimpleResourcedConfig<Float, FloatConfig>() {
    override fun resolve(resources: Resources, resource: Resource): Float {
        //TODO
        return 0f
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getFloat(key)

    override fun setRawValue(source: ConfigSource, key: String, value: Float) {
        source.putFloat(key, value)
    }
}

open class StringConfig : SimpleResourcedConfig<String, StringConfig>() {
    override fun resolve(resources: Resources, resource: Resource): String {
        return resources.getString(resource.resId)
    }

    override fun getRawValue(source: ConfigSource, key: String) = source.getString(key)

    override fun setRawValue(source: ConfigSource, key: String, value: String) {
        source.putString(key, value)
    }
}

class BooleanConfig : SimpleResourcedConfig<Boolean, BooleanConfig>() {
    override fun resolve(resources: Resources, resource: Resource): Boolean {
        return resources.getBoolean(resource.resId)
    }

    override fun getRawValue(source: ConfigSource, key: String) =
            source.getBoolean(key)

    override fun setRawValue(source: ConfigSource, key: String, value: Boolean) {
        source.putBoolean(key, value)
    }
}