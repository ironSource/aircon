package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.utils.Consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type String.
 * The config is converted to an object of the type supplied in {@link #type()}.
 * If the type is generic (e.g Map) then all of the generic type arguments should be supplied in {@link #genericTypes()}.
 * In order to use this config a JsonConverter should be supplied in the SDK configuration.
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
	Class type();

	/**
	 * List of generic types if {@link #type()} requires any.
	 *
	 * @return list of generic types
	 */
	Class[] genericTypes() default {};
}
