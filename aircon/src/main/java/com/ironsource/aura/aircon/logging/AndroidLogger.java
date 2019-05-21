package com.ironsource.aura.aircon.logging;

import android.util.Log;

/**
 * Default logger for the SDK, logs to logcat with "AirCon" tag.
 */
public class AndroidLogger
		implements Logger {

	private static final String TAG = "AirCon";

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
		e.printStackTrace();
	}
}
