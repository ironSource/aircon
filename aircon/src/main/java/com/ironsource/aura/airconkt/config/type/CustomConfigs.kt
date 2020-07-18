package com.ironsource.aura.airconkt.config.type

import android.content.res.Resources
import android.graphics.Color
import android.webkit.URLUtil
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.ConfigDelegate
import com.ironsource.aura.airconkt.config.ResourcesResolver
import com.ironsource.aura.airconkt.config.SimpleConfig
import com.ironsource.aura.airconkt.config.SourceTypeResolver
import com.ironsource.aura.airconkt.logging.Logger
import com.ironsource.aura.airconkt.utils.ColorInt
import com.ironsource.aura.airconkt.utils.Fail
import com.ironsource.aura.airconkt.utils.Success
import com.ironsource.aura.airconkt.utils.getColorHex

fun urlConfig(block: SimpleConfig<String>.() -> Unit) =
        ConfigDelegate(SourceTypeResolver.string(),
                validator = { URLUtil.isValidUrl(it) },
                block = block)

fun textConfig(block: SimpleConfig<String>.() -> Unit) = stringConfig(block)

// TODO Can type erasure fuck me here (if T=List<String>)?
inline fun <reified T> jsonConfig(noinline block: StringConfig<T>.() -> Unit) =
        ConfigDelegate(SourceTypeResolver.string(),
                validator = { it.isNotEmpty() },
                adapter = {
                    val res = AirConKt.get().jsonConverter.fromJson(it, T::class.java)
                    when (res) {
                        is Success -> res.value
                        is Fail -> {
                            log().e("Failed to parse json: $it", res.exception)
                            null
                        }
                    }
                },
                serializer = { AirConKt.get().jsonConverter.toJson(it) },
                block = block
        )

fun colorConfig(block: StringConfig<ColorInt>.() -> Unit) =
        ConfigDelegate(SourceTypeResolver.string(resourcesResolver = ResourcesResolver(Resources::getColorHex)),
                validator = { it.isNotEmpty() },
                adapter = {
                    try {
                        ColorInt(Color.parseColor(it))
                    } catch (e: Exception) {
                        log().e("Failed to parse color hex: $it", e)
                        null
                    }
                },
                serializer = { "#" + Integer.toHexString(it.value) },
                block = block
        )

fun log(): Logger {
    return AirConKt.get()
            .logger
}