package com.ironsource.aura.airconkt.config.type

import android.content.res.Resources
import android.graphics.Color
import android.webkit.URLUtil
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.*
import com.ironsource.aura.airconkt.logging.Logger
import com.ironsource.aura.airconkt.utils.ColorInt
import com.ironsource.aura.airconkt.utils.Fail
import com.ironsource.aura.airconkt.utils.Success
import com.ironsource.aura.airconkt.utils.getColorHex

fun urlConfig(block: PrimitiveConfig<String>.() -> Unit): ConfigProperty<String> =
        ConfigDelegate(SourceTypeResolver.string(),
                validator = { URLUtil.isValidUrl(it) },
                block = block)

fun textConfig(block: PrimitiveConfig<String>.() -> Unit): ConfigProperty<String> =
        stringConfig(block)

inline fun <reified T> jsonConfig(noinline block: Config<String, T>.() -> Unit): ConfigProperty<T> =
        ConfigDelegate(SourceTypeResolver.string(),
                validator = { it.isNotEmpty() },
                adapter = {
                    val res = AirConKt.jsonConverter!!.fromJson(it, T::class.java)
                    when (res) {
                        is Success -> res.value
                        is Fail -> {
                            log()?.e("Failed to parse json: $it", res.exception)
                            null
                        }
                    }
                },
                serializer = { AirConKt.jsonConverter!!.toJson(it) },
                block = block
        )

fun colorConfig(block: Config<String, ColorInt>.() -> Unit): ConfigProperty<ColorInt> =
        ConfigDelegate(SourceTypeResolver.string(resourcesResolver = ResourcesResolver(Resources::getColorHex)),
                validator = { it.isNotEmpty() },
                adapter = {
                    try {
                        ColorInt(Color.parseColor(it))
                    } catch (e: Exception) {
                        log()?.e("Failed to parse color hex: $it", e)
                        null
                    }
                },
                serializer = { "#" + Integer.toHexString(it.value) },
                block = block
        )

fun log(): Logger? {
    return AirConKt
            .logger
}