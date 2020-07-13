package com.ironsource.aura.airconkt.logging;

/**
 * Created on 28/4/19.
 */
public class LoggerWrapper
		implements Logger {

	private final Logger  mLogger;
	private final boolean mEnabled;

	public LoggerWrapper(final Logger logger, final boolean enabled) {
		mLogger = logger;
		mEnabled = enabled;
	}

	@Override
	public void v(final String msg) {
		if (mEnabled) {
			mLogger.v(msg);
		}
	}

	@Override
	public void d(final String msg) {
		if (mEnabled) {
			mLogger.d(msg);
		}
	}

	@Override
	public void w(final String msg) {
		if (mEnabled) {
			mLogger.w(msg);
		}
	}

	@Override
	public void i(final String msg) {
		if (mEnabled) {
			mLogger.i(msg);
		}
	}

	@Override
	public void e(final String msg) {
		if (mEnabled) {
			mLogger.e(msg);
		}
	}

	@Override
	public void logException(final Exception e) {
		if (mEnabled) {
			mLogger.logException(e);
		}
	}
}
