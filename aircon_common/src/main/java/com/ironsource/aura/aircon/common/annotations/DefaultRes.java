package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a resource as a default value for a config
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface DefaultRes {

	/**
	 * Resource identifier from R class (e.g R.string.app_name).
	 * Resource type must match the type of config(e.g @StringConfig can only accept string resources a default)
	 */
	int value();
}
