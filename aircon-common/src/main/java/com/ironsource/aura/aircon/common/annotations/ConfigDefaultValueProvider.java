package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Static methods annotated by this annotation are used as default value providers for configs.
 * This is useful for cases where a default value is not a constant and therefore cannot be
 * supplied as an annotation attribute.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ConfigDefaultValueProvider {

	/**
	 * Config name to provide a default value for.
	 * This value should be another AirCon config constant.
	 * @return config field
	 */
	String value();
}