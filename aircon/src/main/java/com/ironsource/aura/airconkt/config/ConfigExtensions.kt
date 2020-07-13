package com.ironsource.aura.airconkt.config

import kotlin.reflect.KProperty

fun <T, S> Config<T, S>.validated(validator: (T) -> Boolean): Config<T, S> {
    return object : Config<T, S> by this {
        override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): S {
            val value = this@validated.getValue(thisRef, property)
            //TODO
            return value;
        }
    }
}

fun <T, S, S2> Config<T, S>.adapted(adapter: (S) -> S2): Config<T, S2> {
    return object : Config<T, S2> {
        override fun getValue(thisRef: FeatureRemoteConfig, property: KProperty<*>): S2 {
            return adapter(this@adapted.getValue(thisRef, property))
        }

        override fun getDefault(): T {
            return this@adapted.getDefault()
        }
    }
}