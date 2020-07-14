package com.ironsource.aura.airconkt.config

fun <Conf : Config<Byte, *, Conf>> Conf.range(min: Byte = Byte.MIN_VALUE, max: Byte = Byte.MAX_VALUE): Conf {
    return this.constrained { it in min..max }
}

fun <Conf : Config<Short, *, Conf>> Conf.range(min: Short = Short.MIN_VALUE, max: Short = Short.MAX_VALUE): Conf {
    return this.constrained { it in min..max }
}

fun <Conf : Config<Int, *, Conf>> Conf.range(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Conf {
    return this.constrained { it in min..max }
}

fun <Conf : Config<Long, *, Conf>> Conf.range(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Conf {
    return this.constrained { it in min..max }
}

fun <Conf : Config<Float, *, Conf>> Conf.range(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): Conf {
    return this.constrained { it in min..max }
}

fun <Conf : Config<Double, *, Conf>> Conf.range(min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): Conf {
    return this.constrained { it in min..max }
}