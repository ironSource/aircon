package com.ironsource.aura.airconkt.config.type

import android.content.res.Resources
import android.graphics.Color
import android.webkit.URLUtil
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.*
import com.ironsource.aura.airconkt.config.type.util.ColorInt
import com.ironsource.aura.airconkt.utils.Fail
import com.ironsource.aura.airconkt.utils.Success
import com.ironsource.aura.airconkt.utils.getColorHex

fun urlConfig(block: SimpleConfig<String>.() -> Unit) =
        ConfigPropertyFactory.fromPrimitive(SourceTypeResolver.string(),
                validator = { URLUtil.isValidUrl(it) },
                block = block)

fun textConfig(block: SimpleConfig<String>.() -> Unit) =
        stringConfig(block)

inline fun <reified T> jsonConfig(noinline block: Config<String, T>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.string(),
                validator = { it.isNotEmpty() },
                getterAdapter = {
                    val res = AirConKt.jsonConverter!!.fromJson(it, T::class.java)
                    when (res) {
                        is Success -> res.value
                        is Fail -> {
                            AirConKt.logger?.e("Failed to parse json: $it", res.exception)
                            null
                        }
                    }
                },
                setterAdapter = { AirConKt.jsonConverter!!.toJson(it) },
                block = block
        )

fun colorConfig(block: Config<String, ColorInt>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.string(
                resourcesResolver = ResourcesResolver(Resources::getColorHex)),
                validator = { it.isNotEmpty() },
                getterAdapter = {
                    try {
                        ColorInt(
                                Color.parseColor(it))
                    } catch (e: Exception) {
                        AirConKt.logger?.e("Failed to parse color hex: $it", e)
                        null
                    }
                },
                setterAdapter = { "#" + Integer.toHexString(it.value) },
                block = block
        )