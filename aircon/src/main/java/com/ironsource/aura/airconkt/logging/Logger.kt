package com.ironsource.aura.airconkt.logging

/**
 * Logger interface for supplying a custom logger to the SDK.
 *
 * @see com.ironsource.aura.airconkt.Options.Builder.setLogger
 */
interface Logger {
    fun v(msg: String)
    fun d(msg: String)
    fun w(msg: String)
    fun i(msg: String)
    fun e(msg: String, e: Exception? = null)
}