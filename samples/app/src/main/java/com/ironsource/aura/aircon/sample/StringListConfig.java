package com.ironsource.aura.aircon.sample;

import com.ironsource.aura.aircon.common.ConfigType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ConfigType(StringListConfigResolver.class)
public @interface StringListConfig {

    String defaultValue() default "";
}