package com.ironsource.aura.airconkt.source

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.ironsource.aura.airconkt.AirConKt
import java.lang.Boolean.parseBoolean
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.lang.Long.parseLong

private const val PREF_OVERRIDES = "overrides"

/**
 * Created on 4/25/2019.
 */
class FireBaseConfigSource(context: Context,
                           private val remoteConfig: FirebaseRemoteConfig) : ConfigSource {

    private val overridePrefs = context.getSharedPreferences(PREF_OVERRIDES, Context.MODE_PRIVATE)

    override fun getInteger(key: String): Int? {
        return get(key, 0, ::parseInt, SharedPreferences::getInt)
    }

    override fun putInteger(key: String,
                            value: Int?) {
        val edit = overridePrefs.edit()
        if (value != null) {
            edit.putInt(key, value)
        } else {
            edit.remove(key)
        }
        edit.apply()
    }

    override fun getLong(key: String): Long? {
        return get(key, 0, ::parseLong, SharedPreferences::getLong)
    }

    override fun putLong(key: String,
                         value: Long?) {
        val edit = overridePrefs.edit()
        if (value != null) {
            edit.putLong(key, value)
        } else {
            edit.remove(key)
        }
        edit.apply()
    }

    override fun getFloat(key: String): Float? {
        return get(key, 0f, ::parseFloat, SharedPreferences::getFloat)
    }

    override fun putFloat(key: String,
                          value: Float?) {
        val edit = overridePrefs.edit()
        if (value != null) {
            edit.putFloat(key, value)
        } else {
            edit.remove(key)
        }
        edit.apply()
    }

    override fun getBoolean(key: String): Boolean? {
        return get(key, false, {
            val correctType = java.lang.Boolean.TRUE.toString()
                    .equals(it, ignoreCase = true) || java.lang.Boolean.FALSE.toString()
                    .equals(it, ignoreCase = true)
            if (correctType) parseBoolean(it) else null
        }, SharedPreferences::getBoolean)
    }

    override fun putBoolean(key: String,
                            value: Boolean?) {
        val edit = overridePrefs.edit()
        if (value != null) {
            edit.putBoolean(key, value)
        } else {
            edit.remove(key)
        }
        edit.apply()
    }

    override fun getString(key: String): String? {
        return get(key, null, { it }, SharedPreferences::getString)
    }

    override fun putString(key: String,
                           value: String?) {
        overridePrefs.edit()
                .putString(key, value)
                .apply()
    }

    override fun getStringSet(key: String): Set<String>? {
        return getString(key)?.split(",")?.map { it.trim() }?.toSet()
    }

    override fun putStringSet(key: String,
                              value: Set<String>?) {
        overridePrefs.edit()
                .putStringSet(key, value)
                .apply()
    }

    override fun getAny(key: String) = null

    override fun putAny(key: String,
                        value: Any?) {
        // Not supported
    }

    private operator fun <T> get(key: String,
                                 def: T,
                                 parser: (String) -> T?,
                                 prefsResolver: SharedPreferences.(String, T) -> T): T? {
        if (overridePrefs.contains(key)) {
            val overrideValue = overridePrefs.prefsResolver(key, def)
            if (overrideValue != null) {
                log("Using local override value \"$key\" -> $overrideValue")
                return overrideValue
            }
        }
        val strValue = remoteConfig.getString(key)
        return parser(strValue)
    }

    private fun log(msg: String) {
        AirConKt.logger
                ?.v("${toString()} - $msg")
    }
}