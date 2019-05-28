package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.annotations.config.value.RemoteIntValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type int.
 * The config is converted to an Enum const according to the Enum class supplied in {@link #enumClass()}.
 * All enum constants should be annotated by {@link RemoteIntValue} in order to define
 * the mapping.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface IntEnumConfig {

	/**
	 * Return the default value for the config (default: 0).
	 *
	 * @return default config value.
	 */
	int defaultValue() default 0;

	/**
	 * If the configured value will be equal to this value then a random enum const will be returned by the config getter.
	 * Once an random enum const was used, the getter will always return that same const.
	 *
	 * @return random config value
	 */
	int randomizerValue() default Integer.MAX_VALUE;

	/**
	 * The enum class to to convert to.
	 *
	 * @return enum class
	 */
	Class<? extends Enum> enumClass();
}
