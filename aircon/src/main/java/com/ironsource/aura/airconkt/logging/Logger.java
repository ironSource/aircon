package com.ironsource.aura.airconkt.logging;

/**
 * Logger interface for supplying a custom logger to the SDK.
 *
 * @see com.ironsource.aura.airconkt.AirConConfiguration.Builder#setLogger(Logger)
 */
public interface Logger {

	void v(String msg);

	void d(String msg);

	void w(String msg);

	void i(String msg);

	void e(String msg);

	void logException(Exception e);
}
