package com.ironsource.aura.airconkt

import android.content.Context
import com.ironsource.aura.airconkt.logging.Logger
import com.ironsource.aura.airconkt.source.ConfigSourceRepository

/**
 * AirCon SDK entry point.
 */
object AirConKt {

    /**
     * Initializes the SDK with configuration
     */
    fun init(context: Context, block: Options.() -> Unit) {
        if (initialized) {
            return
        }

        this.context = context.applicationContext

        val options = Options(block)

        if (options.loggingOptions.enabled) {
            logger = options.loggingOptions.logger
        }

        jsonConverter = options.jsonConverter
        configSourceRepository = options.configSourceRepository
    }

    private var initialized = ::context.isInitialized

    lateinit var context: Context
        private set

    var logger: Logger? = null
        private set

    var jsonConverter: JsonConverter? = null
        get() {
            checkNotNull(field) { "No json converter available, a converter needs to be supplied in AirCon.init()" }
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