package com.ironsource.aura.aircon

import java.lang.reflect.Type

/**
 * Created on 9/5/2019.
 */
interface JsonConverter {

    fun toJson(obj: Any): String

    @Throws(JsonException::class)
    fun <T> fromJson(json: String, clazz: Class<T>): T?

    @Throws(JsonException::class)
    fun <T> fromJson(json: String, type: Type): T?

    class JsonException : Exception {

        constructor()

        constructor(message: String) : super(message)

        constructor(message: String, cause: Throwable) : super(message, cause)

        constructor(cause: Throwable) : super(cause)
    }
}