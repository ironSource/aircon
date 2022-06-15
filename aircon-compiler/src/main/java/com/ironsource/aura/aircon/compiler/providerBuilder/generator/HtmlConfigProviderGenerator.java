package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.HtmlClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ObjectClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.HtmlConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.CodeBlock;

public class HtmlConfigProviderGenerator
		extends StringConfigProviderGenerator<HtmlConfigElement> {

	HtmlConfigProviderGenerator(final HtmlConfigElement element) {
		super(element);
	}

	@Override
	protected CodeBlock getConversionToTypeExpression(Object varDefaultValue, Object varValue) {
		Object htmlValue = HtmlClassDescriptor.fromHtml(varValue)
		                                      .build();
		htmlValue = ObjectClassDescriptor.from(htmlValue)
		                                 .toStringMethodCall()
		                                 .build();

		final CodeBlock condition = new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_UNEQUALITY, varValue, null)
		                                                  .build();
		final CodeBlock conditionalExpression = new CodeBlockBuilder().addConditionalExpression(condition, htmlValue, varDefaultValue)
		                                                              .build();

		return super.getConversionToTypeExpression(varDefaultValue, conditionalExpression);
	}
}
