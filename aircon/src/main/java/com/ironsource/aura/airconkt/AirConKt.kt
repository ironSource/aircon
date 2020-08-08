package com.ironsource.aura.airconkt

import android.content.Context
import com.ironsource.aura.airconkt.config.type.util.JsonConverter
import com.ironsource.aura.airconkt.logging.AndroidLogger
import com.ironsource.aura.airconkt.logging.Logger
import com.ironsource.aura.airconkt.source.ConfigSource
import com.ironsource.aura.airconkt.source.ConfigSourceRepository
import com.ironsource.aura.dslint.annotations.DSLint

/**
 * AirCon SDK entry point.
 */
object AirConKt {

    /**
     * Initializes the SDK with configuration
     */
    fun init(context: Context,
             block: Options.() -> Unit) {
        if (initialized) {
            return
        }

        this.context = context.applicationContext

        val options = OptionsBuilder(block)

        if (options.loggingOptions.enabled) {
            logger = options.loggingOptions.logger
        }

        if (options.hasJsonConverter()) {
            jsonConverter = options.jsonConverter
        }

        configSourceRepository = options.configSourceRepository
    }

    private var initialized = ::context.isInitialized

    lateinit var context: Context
        private set

    var logger: Logger? = null
        private set

    var jsonConverter: JsonConverter? = null
        get() {
            checkNotNull(
                    field) { "No json converter available, a converter needs to be supplied in AirCon.init()" }
            return field
        }
        private set

    /**
     * Returns config sources repository through which [ConfigSource]
     * can be added, removed and retrieved.
     *
     * @return config source repository.
     * @throws IllegalStateException if SDK is not initialized.
     */
    lateinit var configSourceRepository: ConfigSourceRepository
        private set
}

/**
 * Configuration class used to initialize the SDK.
 *
 * @see AirConKt.init
 */
@DSLint
interface Options {

    /**
     * Json converter to be used with jsonConfig
     */
    var jsonConverter: JsonConverter

    /**
     * Define SDK logging options
     */
    fun logging(block: LoggingOptions.() -> Unit)

    /**
     * Add a config source.
     * A config can have only one instance of the same class.
     */
    fun configSource(configSource: () -> ConfigSource)
}

private class OptionsBuilder : Options {

    companion object {
        operator fun invoke(block: Options.() -> Unit) = OptionsBuilder().apply(
                block)
    }

    override lateinit var jsonConverter: JsonConverter

    internal val configSourceRepository = ConfigSourceRepository()

    internal fun hasJsonConverter() = ::jsonConverter.isInitialized

    internal var loggingOptions: LoggingOptions = LoggingOptionsBuilder {}

    override fun logging(block: LoggingOptions.() -> Unit) {
        loggingOptions = LoggingOptionsBuilder(block)
    }

    override fun configSource(configSource: () -> ConfigSource) {
        configSourceRepository.addSource(configSource())
    }
}

@DSLint
interface LoggingOptions {

    /**
     * Set whether SDK logging is enabled (true by default).
     */
    var enabled: Boolean

    /**
     * Set a logger to be used by the SDK.
     * If a logger is not supplied [com.ironsource.aura.airconkt.logging.AndroidLogger] is used.
     */
    var logger: Logger
}

private class LoggingOptionsBuilder : LoggingOptions {

    companion object {
        operator fun invoke(block: LoggingOptionsBuilder.() -> Unit) = LoggingOptionsBuilder().apply(
                block)
    }

    /**
     * Set whether SDK logging is enabled (true by default).
     */
    override var enabled = true

    /**
     * Set a logger to be used by the SDK.
     * If a logger is not supplied [com.ironsource.aura.airconkt.logging.AndroidLogger] is used.
     */
    override var logger: Logger = AndroidLogger()
}