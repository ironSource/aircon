package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.StringClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.StringConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

/**
 * Created on 11/7/2018.
 */
class StringConfigProviderGenerator <T extends StringConfigElement>
		extends DefaultConfigProviderGenerator<T> {

	StringConfigProviderGenerator(final T element) {
		super(element);
	}

	@Override
	protected CodeBlock getValidationCondition(final String varValue) {
		if (mElement.getType()
		            .equals(TypeName.get(String.class)) && mElement.isEnforceNonEmpty()) {
			final CodeBlock nonNull = new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_UNEQUALITY, varValue, CodeBlockBuilder.NULL)
			                                                .build();
			final CodeBlock isEmpty = new CodeBlockBuilder().addNot()
			                                                .add(StringClassDescriptor.from(varValue)
			                                                                          .isEmpty()
			                                                                          .build())
			                                                .build();
			return new CodeBlockBuilder().addAnd(nonNull, isEmpty)
			                             .build();
		}
		return super.getValidationCondition(varValue);
	}
}