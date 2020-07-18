package com.ironsource.aura.airconkt.logging

/**
 * Created on 28/4/19.
 */
class ControllableLogger(private val logger: Logger,
                         private val enabled: Boolean) : Logger {
    override fun v(msg: String) {
        if (enabled) {
            logger.v(msg)
        }
    }

    override fun d(msg: String) {
        if (enabled) {
            logger.d(msg)
        }
    }

    override fun w(msg: String) {
        if (enabled) {
            logger.w(msg)
        }
    }

    override fun i(msg: String) {
        if (enabled) {
            logger.i(msg)
        }
    }

    override fun e(msg: String, e: Exception?) {
        if (enabled) {
            logger.e(msg, e)
        }
    }
}