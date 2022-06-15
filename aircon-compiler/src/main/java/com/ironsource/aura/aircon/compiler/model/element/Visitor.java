package com.ironsource.aura.aircon.compiler.model.element;

/**
 * Created on 11/3/2018.
 */
public interface Visitor <T, S> {

	S visit(PrimitiveConfigElement configElement, T arg);

	S visit(NumberConfigElement configElement, T arg);

	S visit(StringConfigElement configElement, T arg);

	S visit(TextConfigElement configElement, T arg);

	S visit(StringSetConfigElement configElement, T arg);

	S visit(EnumConfigElement configElement, T arg);

	S visit(TimeConfigElement configElement, T arg);

	S visit(JsonConfigElement configElement, T arg);

	S visit(ColorConfigElement configElement, T arg);

	S visit(UrlConfigElement configElement, T arg);

	S visit(CustomConfigElement configElement, T arg);

	S visit(HtmlConfigElement configElement, T arg);
}
