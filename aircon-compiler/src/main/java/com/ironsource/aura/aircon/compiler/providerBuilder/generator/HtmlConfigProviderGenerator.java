package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.HtmlClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ObjectClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.HtmlConfigElement;
import com.squareup.javapoet.CodeBlock;

public class HtmlConfigProviderGenerator
		extends StringConfigProviderGenerator<HtmlConfigElement> {

	HtmlConfigProviderGenerator(final HtmlConfigElement element) {
		super(element);
	}

	@Override
	protected CodeBlock getConversionToTypeExpression(Object varDefaultValue, Object varValue) {
		varValue = HtmlClassDescriptor.fromHtml(varValue)
		                              .build();
		varValue = ObjectClassDescriptor.from(varValue)
		                                .toStringMethodCall()
		                                .build();
		return super.getConversionToTypeExpression(varDefaultValue, varValue);
	}
}
