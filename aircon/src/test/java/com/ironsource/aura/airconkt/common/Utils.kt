package com.ironsource.aura.airconkt.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import io.mockk.every
import io.mockk.mockk

fun mockContext(): Context {
    val mockk = mockk<Context>()
    every { mockk.applicationContext } returns mockk
    every { mockk.resources } returns mockResource()
    return mockk
}

fun mockResource(): Resources {
    val mockk = mockk<Resources>()
    every { mockk.getInteger(any()) } returns 0
    every { mockk.getFloat(any()) } returns 0f
    every { mockk.getString(any()) } returns ""
    every { mockk.getBoolean(any()) } returns false
    every { mockk.getColor(any()) } returns Color.WHITE
    return mockk
}
