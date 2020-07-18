package com.ironsource.aura.airconkt.source

import com.ironsource.aura.airconkt.SdkContext
import com.ironsource.aura.airconkt.logging.Logger

private val STUB_CONFIG_SOURCE: ConfigSource by lazy { StubConfigSource() }

/**
 * Responsible for managing config sources.
 */
class ConfigSourceRepository internal constructor(private val sdkContext: SdkContext,
                                                  configSources: List<ConfigSource>) {

    private val configSourcesMap = configSources.associateBy { it.javaClass }.toMutableMap()

    /**
     * Add a config source.
     * A config can have only one instance of the same class.
     *
     * @param configSource config source to add
     */
    @Synchronized
    fun addSource(configSource: ConfigSource) {
        configSourcesMap[configSource.javaClass] = configSource
    }

    /**
     * Remove a config source.
     *
     * @param configSource config source to remove
     */
    @Synchronized
    fun removeSource(configSource: ConfigSource) {
        configSourcesMap.remove(configSource.javaClass)
    }

    @Synchronized
    fun getSource(configSourceClass: Class<out ConfigSource?>): ConfigSource {
        val configSource = configSourcesMap[configSourceClass]

        configSource?.let { return it }

        log().v("Unable to find source, returning stub source for class = " + configSourceClass.simpleName)
        return STUB_CONFIG_SOURCE
    }

    private fun log(): Logger {
        return sdkContext.logger
    }
}