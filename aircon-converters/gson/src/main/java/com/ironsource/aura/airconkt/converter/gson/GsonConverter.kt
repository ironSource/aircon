package com.ironsource.aura.airconkt.converter.gson

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.ironsource.aura.airconkt.JsonConverter
import com.ironsource.aura.airconkt.JsonException
import com.ironsource.aura.airconkt.utils.Fail
import com.ironsource.aura.airconkt.utils.Response
import com.ironsource.aura.airconkt.utils.Success
import java.lang.reflect.Type

class GsonConverter constructor(private val gson: Gson = Gson()) : JsonConverter {

    override fun toJson(obj: Any?): String? {
        return gson.toJson(obj)
    }

    override fun <T> fromJson(json: String?, clazz: Class<T>): Response<T, JsonException> {
        return try {
            Success(gson.fromJson(json, clazz))
        } catch (e: JsonSyntaxException) {
            Fail(JsonException(e))
        }
    }

    override fun <T> fromJson(json: String?, type: Type): Response<T, JsonException> {
        return try {
            Success(gson.fromJson(json, type))
        } catch (e: JsonSyntaxException) {
            Fail(JsonException(e))
        }
    }
}