package com.ironsource.aura.aircon.compiler.model.element;

/**
 * Created on 11/3/2018.
 */
public class StringConfigElement
		extends PrimitiveConfigElement {

	private final boolean mEnforceNonEmpty;

	StringConfigElement(Properties properties, final boolean enforceNonEmpty) {
		super(properties);
		mEnforceNonEmpty = enforceNonEmpty;
	}

	public boolean isEnforceNonEmpty() {
		return mEnforceNonEmpty;
	}

	@Override
	public <T, S> S accept(final Visitor<T, S> visitor, final T arg) {
		return visitor.visit(this, arg);
	}
}
