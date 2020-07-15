package com.ironsource.aura.airconkt.config.constraint

import com.ironsource.aura.airconkt.config.ReadOnlyConfig

fun <Conf : ReadOnlyConfig<Int, *>> Conf.rangeConstraint(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) {
    return this.constraint { it in min..max }
}

fun <Conf : ReadOnlyConfig<Long, *>> Conf.rangeConstraint(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE) {
    return this.constraint { it in min..max }
}

fun <Conf : ReadOnlyConfig<Float, *>> Conf.rangeConstraint(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE) {
    return this.constraint { it in min..max }
}