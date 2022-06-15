package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.utils.Consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type String. This config returns displayable styled text from the provided HTML string.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface HtmlConfig {

	/**
	 * Return whether empty String values are supported or default value should be returned.
	 * @return true if empty Strings are not supported
	 */
	boolean enforceNonEmpty() default false;

	/**
	 * Return the default value for the config (default: null).
	 * @return default config value.
	 */
	String defaultValue() default Consts.NULL_STRING;
}
