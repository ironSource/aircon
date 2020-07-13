package com.ironsource.aura.airconkt.config

interface Constraint<T> {
    fun isValid(value: T): Boolean
}

fun <Conf : AbstractConfig<Conf, Byte, *>> Conf.range(min: Byte = Byte.MIN_VALUE, max: Byte = Byte.MAX_VALUE): Conf {
    return this.constraint { it in min..max }
}

fun <Conf : AbstractConfig<Conf, Short, *>> Conf.range(min: Short = Short.MIN_VALUE, max: Short = Short.MAX_VALUE): Conf {
    return this.constraint { it in min..max }
}

fun <Conf : AbstractConfig<Conf, Int, *>> Conf.range(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Conf {
    return this.constraint { it in min..max }
}

fun <Conf : AbstractConfig<Conf, Long, *>> Conf.range(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Conf {
    return this.constraint { it in min..max }
}

fun <Conf : AbstractConfig<Conf, Float, *>> Conf.range(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): Conf {
    return this.constraint { it in min..max }
}

fun <Conf : AbstractConfig<Conf, Double, *>> Conf.range(min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): Conf {
    return this.constraint { it in min..max }
}