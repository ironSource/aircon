package com.ironsource.aura.airconkt.sample

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ironsource.aura.airconkt.AirConKt
import com.ironsource.aura.airconkt.converter.gson.GsonConverter
import com.ironsource.aura.airconkt.logging.Logger
import com.ironsource.aura.airconkt.source.FireBaseConfigSource

private const val TAG = "AirConSample"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build())

        AirConKt.init(this) {
            jsonConverter = GsonConverter()
            logging {
                enabled = BuildConfig.DEBUG
                logger = getLogger()
            }
            configSource { FireBaseConfigSource(this@App, firebaseRemoteConfig) }
        }
    }
}

private fun getLogger(): Logger {
    return object : Logger {
        override fun v(msg: String) {
            Log.v(TAG, msg)
        }

        override fun d(msg: String) {
            Log.d(TAG, msg)
        }

        override fun w(msg: String) {
            Log.w(TAG, msg)
        }

        override fun i(msg: String) {
            Log.i(TAG, msg)
        }

        override fun e(msg: String, e: Exception?) {
            Log.e(TAG, msg)
            e?.printStackTrace()
        }
    }
}