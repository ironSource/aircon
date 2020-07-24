package com.ironsource.aura.airconkt.sample.config

import com.ironsource.aura.airconkt.config.Config
import com.ironsource.aura.airconkt.config.ConfigPropertyFactory
import com.ironsource.aura.airconkt.config.SourceTypeResolver

data class Label(val value: String)

fun labelConfig(block: Config<String, Label>.() -> Unit) =
        ConfigPropertyFactory.from(SourceTypeResolver.string(),
                validator = { it.isNotEmpty() },
                getterAdapter = { Label(it) },
                setterAdapter = { it.value },
                block = block
        )