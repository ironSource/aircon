package com.ironsource.aura.aircon.common;

import java.lang.annotation.Annotation;

public interface ConfigTypeResolver <A extends Annotation, T, S> {

	Class<A> getAnnotationClass();

	boolean isValid(A annotation, T value);

	S process(A annotation, T value);
}
