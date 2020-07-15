package com.ironsource.aura.airconkt.config.constraint

interface Constraint<T> {
    fun isValid(value: T): Boolean
}