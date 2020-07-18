package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.logging.AndroidLogger
import com.ironsource.aura.airconkt.logging.Logger
import com.ironsource.aura.airconkt.source.ConfigSource
import com.ironsource.aura.airconkt.source.ConfigSourceRepository

/**
 * Configuration class used to initialize the SDK.
 *
 * @see AirConKt.init
 */
class Options private constructor() {
    companion object {
        operator fun invoke(block: Options.() -> Unit) = Options().apply(block)
    }

    /**
     * Json converter to be used with jsonConfig
     */
    lateinit var jsonConverter: JsonConverter

    internal var loggingOptions = LoggingOptions {}
        private set

    internal val configSourceRepository = ConfigSourceRepository()

    /**
     * Define SDK logging options
     */
    fun logging(block: LoggingOptions.() -> Unit) {
        loggingOptions = LoggingOptions(block)
    }

    /**
     * Add a config source.
     * A config can have only one instance of the same class.
     */
    fun configSource(configSource: () -> ConfigSource) {
        configSourceRepository.addSource(configSource())
    }
}

class LoggingOptions private constructor() {
    companion object {
        operator fun invoke(block: LoggingOptions.() -> Unit) = LoggingOptions().apply(block)
    }

    /**
     * Set whether SDK logging is enabled (true by default).
     */
    var enabled = true

    /**
     * Set a logger to be used by the SDK.
     * If a logger is not supplied [com.ironsource.aura.airconkt.logging.AndroidLogger] is used.
     */
    var logger: Logger = AndroidLogger()
}

