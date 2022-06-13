package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.HtmlClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.StyledTextConfigElement;
import com.squareup.javapoet.CodeBlock;

public class StyledTextConfigProviderGenerator
		extends StringConfigProviderGenerator<StyledTextConfigElement> {

	StyledTextConfigProviderGenerator(final StyledTextConfigElement element) {
		super(element);
	}

	@Override
	protected CodeBlock getConversionToTypeExpression(Object varDefaultValue, Object varValue) {
		varValue = HtmlClassDescriptor.fromHtml(varValue);
		return super.getConversionToTypeExpression(varDefaultValue, varValue);
	}
}
