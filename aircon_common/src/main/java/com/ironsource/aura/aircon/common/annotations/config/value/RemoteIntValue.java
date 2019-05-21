package com.ironsource.aura.aircon.common.annotations.config.value;

import com.ironsource.aura.aircon.common.annotations.config.IntEnumConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define the int value of an enum constant as received from the config.
 * @see IntEnumConfig
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface RemoteIntValue {

	/**
	 * Constant int value.
	 * @return int value.
	 */
	int value();
}
