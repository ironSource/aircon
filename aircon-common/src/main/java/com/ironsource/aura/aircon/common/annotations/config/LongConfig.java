package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A config of type long.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface LongConfig {

	/**
	 * Min allowed value (included) for config (default Long.MIN_VALUE).
	 * @see #minValueFallbackPolicy()
	 * @return min config value.
	 */
	long minValue() default Long.MIN_VALUE;

	/**
	 * Max allowed value (included) for config (default Long.MAX_VALUE).
	 * @see #maxValueFallbackPolicy()
	 * @return max config value.
	 */
	long maxValue() default Long.MAX_VALUE;

	/**
	 * Define the policy when the config value is smaller than the value defined by {@link #minValue()}.
	 * @return fallback policy
	 */
	RangeFallbackPolicy minValueFallbackPolicy() default RangeFallbackPolicy.DEFAULT;

	/**
	 * Define the policy when the config value is greater than the value defined by {@link #maxValue()} ()}.
	 * @return fallback policy
	 */
	RangeFallbackPolicy maxValueFallbackPolicy() default RangeFallbackPolicy.DEFAULT;

	/**
	 * Return the default value for the config (default: 0).
	 * @return default config value.
	 */
	long defaultValue() default 0;

}
