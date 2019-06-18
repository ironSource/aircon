package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Static methods annotated by this annotation are used as to mock config values for testing purposes.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ConfigMock {

	/**
	 * Config name to set serve as a mock provider for.
	 * This value should be another AirCon config constant.
	 * @return config field
	 */
	String value();
}