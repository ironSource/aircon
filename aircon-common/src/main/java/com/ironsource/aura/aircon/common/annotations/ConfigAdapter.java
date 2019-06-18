package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Static methods annotated by this annotation are used as adapters for configs.
 * This is useful for cases where the returned config value should be processed and/or converted to
 * a different type.
 * For common cases use dedicated config annotations instead (e.g {@link com.ironsource.aura.aircon.common.annotations.config.JsonConfig})
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ConfigAdapter {

	/**
	 * Config name to set serve as an adapter for.
	 * This value should be another AirCon config constant.
	 * @return config field
	 */
	String value();
}