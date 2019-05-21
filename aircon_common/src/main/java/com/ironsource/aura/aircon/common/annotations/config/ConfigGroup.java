package com.ironsource.aura.aircon.common.annotations.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A group of configs.
 * A class and a provider method with the name of the annotated config will be generated.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface ConfigGroup {

	/**
	 * Configs to include in groups.
	 * Values should be fields annotated by one of the config annotations.
	 * @return array of configs to group.
	 */
	String[] value();
}
