package com.ironsource.aura.aircon.compiler.model.element;

import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/3/2018.
 */
public class JsonConfigElement
		extends ConfigElement {

	private final TypeMirror mJsonType;

	JsonConfigElement(Properties properties, final TypeMirror jsonType) {
		super(properties);
		mJsonType = jsonType;
	}

	public TypeMirror getJsonType() {
		return mJsonType;
	}

	@Override
	public <V, S> S accept(final Visitor<V, S> visitor, final V arg) {
		return visitor.visit(this, arg);
	}
}
