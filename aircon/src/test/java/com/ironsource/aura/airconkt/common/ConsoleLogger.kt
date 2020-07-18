package com.ironsource.aura.airconkt.common

import com.ironsource.aura.airconkt.logging.Logger

class ConsoleLogger : Logger {

    override fun v(msg: String) {
        log("v", msg)
    }

    override fun d(msg: String) {
        log("d", msg)
    }

    override fun w(msg: String) {
        log("w", msg)
    }

    override fun i(msg: String) {
        log("i", msg)
    }

    override fun e(msg: String, e: Exception?) {
        log("e", msg)
        e?.printStackTrace()
    }

    private fun log(lvl: String, msg: String) {
        println("$lvl: $msg")
    }
}