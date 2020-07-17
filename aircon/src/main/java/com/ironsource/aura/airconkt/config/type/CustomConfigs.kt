package com.ironsource.aura.airconkt.config.type

import android.content.res.Resources
import android.webkit.URLUtil
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.config.ConfigDelegate
import com.ironsource.aura.airconkt.config.ResourcesResolver
import com.ironsource.aura.airconkt.config.SimpleConfig
import com.ironsource.aura.airconkt.config.SourceTypeResolver
import com.ironsource.aura.airconkt.utils.AirConUtils
import com.ironsource.aura.airconkt.utils.ColorInt
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
                adapter = { AirConKt.get().jsonConverter.fromJson(it, T::class.java) },
                serializer = { AirConKt.get().jsonConverter.toJson(it) },
                block = block
        )

fun colorConfig(block: StringConfig<ColorInt>.() -> Unit) =
        ConfigDelegate(SourceTypeResolver.string(resourcesResolver = ResourcesResolver(Resources::getColorHex)),
                adapter = { AirConUtils.hexToColorInt(it) },
                serializer = { AirConUtils.colorIntToHex(it) },
                block = block
        )