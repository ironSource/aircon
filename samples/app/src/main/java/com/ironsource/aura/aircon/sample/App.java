package com.ironsource.aura.aircon.sample;

import android.app.Application;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ironsource.aura.aircon.AirCon;
import com.ironsource.aura.aircon.AirConConfiguration;
import com.ironsource.aura.aircon.converter.gson.GsonConverter;
import com.ironsource.aura.aircon.logging.Logger;
import com.ironsource.aura.aircon.source.FireBaseConfigSource;

/**
 * Created on 11/9/2018.
 */
public class App
		extends Application {

	public static final String TAG = "AirConSample";

	@Override
	public void onCreate() {
		super.onCreate();
		initAirConSdk();
	}

	private void initAirConSdk() {
		final AirCon airCon = AirCon.get();
		final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
		firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
		                                                                                 .build());
		final FireBaseConfigSource configSource = new FireBaseConfigSource(this, firebaseRemoteConfig);
		airCon.init(new AirConConfiguration.Builder(this).enableXmlInjection(R.attr.class, configSource)
		                                                 .setJsonConverter(new GsonConverter())
		                                                 .setLogger(getLogger())
		                                                 .setLoggingEnabled(BuildConfig.DEBUG)
		                                                 .addConfigSource(configSource)
		                                                 .build());
	}

	private Logger getLogger() {
		return new Logger() {
			@Override
			public void v(final String msg) {
				Log.v(TAG, msg);
			}

			@Override
			public void d(final String msg) {
				Log.d(TAG, msg);
			}

			@Override
			public void w(final String msg) {
				Log.w(TAG, msg);
			}

			@Override
			public void i(final String msg) {
				Log.i(TAG, msg);
			}

			@Override
			public void e(final String msg) {
				Log.e(TAG, msg);
			}

			@Override
			public void logException(final Exception e) {
				Log.e(TAG, e.toString());
			}
		};
	}
}