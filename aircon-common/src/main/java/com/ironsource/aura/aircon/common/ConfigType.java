package com.ironsource.aura.aircon.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to denote a custom config type annotation.
 * Any custom config type annotation should also define a {@link ConfigTypeResolver} and register it
 * via the AirConConfiguration used to init the SDK.
 * Custom config type annotations must defined a "defaultValue" attribute of one of the following types: String, float, int, long or boolean
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ConfigType {

	/**
	 * The config resolver for this custom config type.
	 * The config type resolver generic type must match the annotation annotated with this annotation.
	 * @return config type resolver
	 */
	Class<? extends ConfigTypeResolver> value();
}
