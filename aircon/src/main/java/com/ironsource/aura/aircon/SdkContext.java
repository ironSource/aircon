package com.ironsource.aura.aircon;

import com.ironsource.aura.aircon.logging.Logger;

/**
 * Internal SDK context used to limit creation of public yet internal classes.
 * This class should not be used outside the SDK internal logic.
 */
public class SdkContext {

	private final Logger mLogger;

	SdkContext(final Logger logger) {
		mLogger = logger;
	}

	public Logger getLogger() {
		return mLogger;
	}
}
