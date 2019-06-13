package com.ironsource.aura.aircon.sample;

import com.ironsource.aura.aircon.common.ConfigType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@ConfigType(MapConfigTypeResolver.class)
public @interface MapConfig {

	String defaultValue() default "";
}