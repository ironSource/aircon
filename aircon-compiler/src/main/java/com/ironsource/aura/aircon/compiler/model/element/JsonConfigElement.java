package com.ironsource.aura.aircon.compiler.model.element;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * Created on 11/3/2018.
 */
public class JsonConfigElement
		extends ConfigElement {

	JsonConfigElement(Properties properties) {
		super(properties);
	}

	public TypeName getJsonType() {
		return getType();
	}

	public boolean isGenericType() {
		return getJsonType() instanceof ParameterizedTypeName;
	}

	@Override
	public <V, S> S accept(final Visitor<V, S> visitor, final V arg) {
		return visitor.visit(this, arg);
	}
}
