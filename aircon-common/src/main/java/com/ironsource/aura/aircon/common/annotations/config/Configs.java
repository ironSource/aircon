package com.ironsource.aura.aircon.common.annotations.config;

import com.ironsource.aura.aircon.common.utils.Utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 10/20/2018.
 */
public interface Configs {

	List<Class<? extends Annotation>> PRIMITIVES = Arrays.asList(BooleanConfig.class, IntConfig.class, LongConfig.class, FloatConfig.class, StringConfig.class, StringSetConfig.class);
	List<Class<? extends Annotation>> ENUM       = Arrays.asList(IntEnumConfig.class, StringEnumConfig.class);
	List<Class<? extends Annotation>> COMPLEX    = Arrays.asList(TimeConfig.class, JsonConfig.class, ColorConfig.class, UrlConfig.class, TextConfig.class, StyledTextConfig.class);

	List<Class<? extends Annotation>> ALL = Utils.concatLists(PRIMITIVES, ENUM, COMPLEX);
}
