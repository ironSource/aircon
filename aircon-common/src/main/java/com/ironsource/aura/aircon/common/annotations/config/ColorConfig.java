package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.utils.Consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type String (color Hex).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface ColorConfig {

	/**
	 * Return the default value for the config (default: null).
	 * @return default config value.
	 */
	String defaultValue() default Consts.NULL_STRING;
}
