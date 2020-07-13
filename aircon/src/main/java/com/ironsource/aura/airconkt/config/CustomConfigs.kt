package com.ironsource.aura.airconkt.config

import android.content.res.Resources
import android.webkit.URLUtil
import com.ironsource.aura.airconkt.source.ConfigSource
import com.ironsource.aura.airconkt.utils.AirConUtils
import com.ironsource.aura.airconkt.utils.ColorInt

class UrlConfig : StringConfig() {
    override fun isValid(value: String): Boolean {
        return URLUtil.isValidUrl(value)
    }
}

class TextConfig : StringConfig()

class ColorConfig : ResourcedConfig<ColorConfig, String, ColorInt>() {
    override fun resolve(resources: Resources, resource: Resource): String {
        return asRaw(ColorInt(resources.getColor(resource.resId)))
    }

    override fun getRawValue(source: ConfigSource, key: String): String? {
        return source.getString(key)
    }

    override fun setRawValue(source: ConfigSource, key: String, value: String) {
        source.putString(key, value)
    }

    override fun asRaw(value: ColorInt): String {
        return AirConUtils.colorIntToHex(value)
    }

    override fun adapt(value: String): ColorInt {
        return AirConUtils.hexToColorInt(value)
    }

    override fun isValid(value: String): Boolean {
        return AirConUtils.hexToColorInt(value) != null
    }
}