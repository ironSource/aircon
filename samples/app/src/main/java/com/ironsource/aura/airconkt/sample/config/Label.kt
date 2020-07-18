package com.ironsource.aura.airconkt.sample.config

import com.ironsource.aura.airconkt.config.ConfigDelegate
import com.ironsource.aura.airconkt.config.SourceTypeResolver
import com.ironsource.aura.airconkt.config.type.StringConfig

data class Label(val value: String)

fun labelConfig(block: StringConfig<Label>.() -> Unit) =
        ConfigDelegate(SourceTypeResolver.string(),
                validator = { it.isNotEmpty() },
                adapter = { Label(it) },
                serializer = { it.value },
                block = block
        )