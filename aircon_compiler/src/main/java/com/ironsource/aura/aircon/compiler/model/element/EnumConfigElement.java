package com.ironsource.aura.aircon.compiler.model.element;

import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/3/2018.
 */
public class EnumConfigElement
		extends ConfigElement {

	private final TypeMirror mEnumClass;
	private final Integer    mRandomizerValue;

	EnumConfigElement(Properties properties, final TypeMirror enumClass, final Integer randomizerValue) {
		super(properties);
		mEnumClass = enumClass;
		mRandomizerValue = randomizerValue;
	}

	public TypeMirror getEnumClass() {
		return mEnumClass;
	}

	public boolean hasRandomizerValue() {
		return mRandomizerValue != null;
	}

	public Integer getRandomizerValue() {
		return mRandomizerValue;
	}

	@Override
	public <V, S> S accept(final Visitor<V, S> visitor, final V arg) {
		return visitor.visit(this, arg);
	}
}
