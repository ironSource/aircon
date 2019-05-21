package com.ironsource.aura.aircon.common.annotations.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type Set<String>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface StringSetConfig {

	/**
	 * Return the default value for the config (default: empty set).
	 * @return default config value.
	 */
	String[] defaultValue() default {};
}
