package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Static predicate methods annotated by this annotation are used as validators for the returned config
 * value.
 * If the predicate method return false then the default value will be returned by the config provider.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ConfigValidator {

	/**
	 * Config name to set serve as a validator for.
	 * This value should be another AirCon config constant.
	 * @return config field
	 */
	String value();
}