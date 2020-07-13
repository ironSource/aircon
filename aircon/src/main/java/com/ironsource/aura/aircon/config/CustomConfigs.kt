package com.ironsource.aura.aircon.config

import android.content.res.Resources
import android.webkit.URLUtil
import com.ironsource.aura.aircon.source.ConfigSource
import com.ironsource.aura.aircon.utils.AirConUtils
import com.ironsource.aura.aircon.utils.ColorInt

class UrlConfig : StringConfig() {
    override fun isValid(value: String): Boolean {
        return URLUtil.isValidUrl(value)
    }
}

class TextConfig : StringConfig()

class ColorConfig : ResourcedConfig<String, ColorInt>() {
    override fun resolve(resources: Resources, resource: Resource): String {
        return AirConUtils.colorIntToHex(ColorInt(resources.getColor(resource.resId)))
    }

    override fun getValue(source: ConfigSource, key: String, defaultValue: String): String? {
        return source.getString(key, defaultValue)
    }

    override fun adapt(value: String) = AirConUtils.hexToColorInt(value)
}