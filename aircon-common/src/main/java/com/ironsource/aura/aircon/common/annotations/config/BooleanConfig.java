package com.ironsource.aura.aircon.common.annotations.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type boolean.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BooleanConfig {

	/**
	 * Return the default value for the config (default: false).
	 * @return default config value.
	 */
	boolean defaultValue() default false;
}
