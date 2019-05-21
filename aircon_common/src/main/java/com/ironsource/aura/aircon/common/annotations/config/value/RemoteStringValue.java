package com.ironsource.aura.aircon.common.annotations.config.value;

import com.ironsource.aura.aircon.common.annotations.config.StringEnumConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define the String value of an enum constant as received from the config.
 *
 * @see StringEnumConfig
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface RemoteStringValue {

	/**
	 * Constant String value.
	 * @return String value.
	 */
	String value();
}
