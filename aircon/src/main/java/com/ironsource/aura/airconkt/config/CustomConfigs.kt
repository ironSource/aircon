package com.ironsource.aura.airconkt.config

import android.content.res.Resources
import android.webkit.URLUtil
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.utils.AirConUtils
import com.ironsource.aura.airconkt.utils.ColorInt
import com.ironsource.aura.airconkt.utils.getColorHex

fun urlConfig(block: ReadWriteConfig<String, String>.() -> Unit) = createConfig(block) {
    PrimitiveConfigDelegate(
            configSourceResolver = ConfigSourceResolver.String,
            resourcesResolver = ResourcesResolver.String,
            validator = {
                URLUtil.isValidUrl(it)
            })
}

fun textConfig(block: ReadWriteConfig<String, String>.() -> Unit) = stringConfig(block)

// TODO Can type erasure fuck me here (if T=List<String>)?
inline fun <reified T> jsonConfig(noinline block: ReadWriteConfigDelegate<String, T>.() -> Unit) = createConfig(block) {
    ReadWriteConfigDelegate(
            configSourceResolver = ConfigSourceResolver.String,
            resourcesResolver = ResourcesResolver.String,
            validator = { it.isNotEmpty() },
            adapter = { AirConKt.get().jsonConverter.fromJson(it, T::class.java) },
            serializer = { AirConKt.get().jsonConverter.toJson(it) }
    )
}

fun colorConfig(block: ReadWriteConfigDelegate<String, ColorInt>.() -> Unit) = createConfig(block) {
    ReadWriteConfigDelegate(
            configSourceResolver = ConfigSourceResolver.String,
            resourcesResolver = ResourcesResolver(Resources::getColorHex),
            validator = { AirConUtils.hexToColorInt(it) != null },
            adapter = { AirConUtils.hexToColorInt(it) },
            serializer = { AirConUtils.colorIntToHex(it) }
    )
}