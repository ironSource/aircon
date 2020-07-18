package com.ironsource.aura.airconkt.utils

import android.content.res.Resources
import android.util.TypedValue

fun Resources.getLong(id: Int) = this.getInteger(id).toLong()

fun Resources.getFloat(id: Int): Float {
    val typedValue = TypedValue()
    getValue(id, typedValue, true)
    return typedValue.float
}

fun Resources.getColorHex(id: Int) =
        "#" + Integer.toHexString(getColor(id))
