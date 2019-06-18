package com.ironsource.aura.aircon.compiler.model.element;

import com.squareup.javapoet.TypeName;

/**
 * Created on 11/3/2018.
 */
public class CustomConfigElement
		extends ConfigElement {

	private final TypeName mAnnotation;

	CustomConfigElement(Properties properties, TypeName annotation) {
		super(properties);
		mAnnotation = annotation;
	}

	public TypeName getAnnotation() {
		return mAnnotation;
	}

	@Override
	public <T, S> S accept(final Visitor<T, S> visitor, final T arg) {
		return visitor.visit(this, arg);
	}
}
