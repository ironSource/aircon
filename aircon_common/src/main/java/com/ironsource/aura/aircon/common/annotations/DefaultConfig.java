package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define the default value of a config to the configured value of another config.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface DefaultConfig {

	/**
	 * Config name to use as default.
	 * This value should be another AirCon config constant.
	 */
	String value();
}
