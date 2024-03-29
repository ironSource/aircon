package com.ironsource.aura.aircon.compiler.model.element;

public class HtmlConfigElement
		extends StringConfigElement {

	HtmlConfigElement(Properties properties, boolean enforceNonEmpty) {
		super(properties, enforceNonEmpty);
	}

	@Override
	public <T, S> S accept(Visitor<T, S> visitor, T arg) {
		return visitor.visit(this, arg);
	}
}
