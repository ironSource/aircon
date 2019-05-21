package com.ironsource.aura.aircon.compiler.model.element;

/**
 * Created on 11/3/2018.
 */
public class UrlConfigElement
		extends StringConfigElement {

	UrlConfigElement(Properties properties, final boolean enforceNonEmpty) {
		super(properties, enforceNonEmpty);
	}

	@Override
	public <T, S> S accept(final Visitor<T, S> visitor, final T arg) {
		return visitor.visit(this, arg);
	}
}
