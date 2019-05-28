package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;
import com.ironsource.aura.aircon.compiler.descriptors.MathClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.NumberConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;


class NumberConfigProviderGenerator <T extends NumberConfigElement>
		extends PrimitiveConfigProviderGenerator<T> {

	NumberConfigProviderGenerator(final T element) {
		super(element);
	}

	@Override
	public CodeBlock getConversionToTypeExpression(final Object varDefaultValue, Object varValue) {
		if (mElement.hasMinValue() && mElement.getMinValueFallbackPolicy() == RangeFallbackPolicy.RANGE_VALUE) {
			varValue = MathClassDescriptor.max(varValue, getMinValue())
			                              .build();
		}

		if (mElement.hasMaxValue() && mElement.getMaxValueFallbackPolicy() == RangeFallbackPolicy.RANGE_VALUE) {
			varValue = MathClassDescriptor.min(varValue, getMaxValue())
			                              .build();
		}

		return super.getConversionToTypeExpression(varDefaultValue, varValue);
	}

	@Override
	protected CodeBlock getValidationCondition(final String varValue) {
		final CodeBlockBuilder codeBlockBuilder = new CodeBlockBuilder();
		final List<CodeBlock> conditions = new ArrayList<>();

		if (mElement.hasMinValue() && mElement.getMinValueFallbackPolicy() == RangeFallbackPolicy.DEFAULT) {
			final CodeBlock minCondition = getRangeCondition(varValue, getMinValue());
			conditions.add(minCondition);
		}

		if (mElement.hasMaxValue() && mElement.getMaxValueFallbackPolicy() == RangeFallbackPolicy.DEFAULT) {
			final CodeBlock maxCondition = getRangeCondition(getMaxValue(), varValue);
			conditions.add(maxCondition);
		}

		return !conditions.isEmpty() ? codeBlockBuilder.addAnd(conditions.toArray())
		                                               .build() : super.getValidationCondition(varValue);
	}

	private CodeBlock getRangeCondition(final Object var1, final Object varValue2) {
		return new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_GREATER_THAN_OR_EQUALS, var1, varValue2)
		                             .build();
	}

	private Object getMaxValue() {
		return castIfNeeded(mElement.getMaxValue());
	}

	private Object getMinValue() {
		return castIfNeeded(mElement.getMinValue());
	}

	private Object castIfNeeded(Number number) {
		return number instanceof Float ? new CodeBlockBuilder().addPrimitiveCaster(TypeName.FLOAT, number)
		                                                       .build() : number;
	}
}