package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.annotations.ConfigAdapter;
import com.ironsource.aura.aircon.common.utils.Consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type String.
 * The config is converted to an object of the type supplied in {@link #type()} using Gson.
 * This doesn't support generic types (List<Integer>), for that use {@link ConfigAdapter}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface JsonConfig {

	/**
	 * Return the default value for the config (default: null).
	 *
	 * @return default config value.
	 */
	String defaultValue() default Consts.NULL_STRING;

	/**
	 * The config Json type.
	 *
	 * @return json class.
	 */
	Class<?> type();
}
