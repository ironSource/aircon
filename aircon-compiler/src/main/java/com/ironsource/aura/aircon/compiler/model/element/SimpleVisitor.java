package com.ironsource.aura.aircon.compiler.model.element;

/**
 * Created on 11/3/2018.
 */
public abstract class SimpleVisitor <T, S>
		implements Visitor<T, S> {

	@Override
	public S visit(final PrimitiveConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final NumberConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final TextConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final StringConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final UrlConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final StringSetConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final ColorConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final EnumConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final TimeConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final JsonConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(final CustomConfigElement configElement, final T arg) {
		return defaultVisit(configElement, arg);
	}

	@Override
	public S visit(HtmlConfigElement configElement, T arg) {
		return defaultVisit(configElement, arg);
	}

	protected abstract S defaultVisit(final ConfigElement configElement, final T arg);
}
