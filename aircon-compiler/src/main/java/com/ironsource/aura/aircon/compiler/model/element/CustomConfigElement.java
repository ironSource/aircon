package com.ironsource.aura.aircon.compiler.model.element;

import javax.lang.model.element.AnnotationMirror;

/**
 * Created on 11/3/2018.
 */
public class CustomConfigElement
		extends ConfigElement {

	private final AnnotationMirror mAnnotationTypeMirror;

	CustomConfigElement(Properties properties, AnnotationMirror annotationTypeMirror) {
		super(properties);
		mAnnotationTypeMirror = annotationTypeMirror;
	}

	public AnnotationMirror getAnnotationTypeMirror() {
		return mAnnotationTypeMirror;
	}

	@Override
	public <T, S> S accept(final Visitor<T, S> visitor, final T arg) {
		return visitor.visit(this, arg);
	}
}
