package com.ironsource.aura.aircon.common.annotations;

import com.ironsource.aura.aircon.common.ConfigSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the source the config annotated by this annotation.
 * Config sources can be added to the AirCon SDK instance.
 * If a config source is used and not added to the SDK then default values will be returned for all
 * configs associated with this source.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Source {

	/**
	 * The class object of the config source.
	 */
	Class<? extends ConfigSource> value();
}
