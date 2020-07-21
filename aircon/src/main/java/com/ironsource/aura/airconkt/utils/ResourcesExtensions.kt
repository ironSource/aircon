package com.ironsource.aura.airconkt.utils

import android.content.res.Resources
import android.util.TypedValue

internal fun Resources.getLong(id: Int) = this.getInteger(id).toLong()

internal fun Resources.getFloat(id: Int): Float {
    val typedValue = TypedValue()
    getValue(id, typedValue, true)
    return typedValue.float
}

internal fun Resources.getAny(id: Int): Any {
    return getString(id)
}

internal fun Resources.getColorHex(id: Int) =
        "#" + Integer.toHexString(getColor(id))
