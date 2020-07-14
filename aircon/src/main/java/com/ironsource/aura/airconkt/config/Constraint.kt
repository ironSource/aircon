package com.ironsource.aura.airconkt.config

interface Constraint<T> {
    fun isValid(value: T): Boolean
}