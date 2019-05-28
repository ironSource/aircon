package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.descriptors.SystemClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.TimeUnitClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.TimeConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created on 11/7/2018.
 */
class TimeConfigProviderGenerator
		extends NumberConfigProviderGenerator<TimeConfigElement> {

	private static final String DURING_DURATION_PREDICATE_SUFFIX = "During";

	TimeConfigProviderGenerator(final TimeConfigElement configElement) {
		super(configElement);
	}

	@Override
	public List<MethodSpec> createAdditionalPredicates(final ClassName className) {
		return Collections.singletonList(createDurationPassedPredicate());
	}

	private MethodSpec createDurationPassedPredicate() {
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(getDuringDurationPredicateMethodName())
		                                             .addModifiers(getConfigMethodModifiers())
		                                             .returns(TypeName.BOOLEAN);

		final Set<ParameterSpec> parameterSpecs = getConfigMethodParameters();
		builder.addParameters(parameterSpecs);

		return builder.addCode(getDuringDurationPredicateBodyCodeBlock())
		              .build();
	}

	private String getDuringDurationPredicateMethodName() {
		return Consts.PREDICATE_METHOD_PREFIX + DURING_DURATION_PREDICATE_SUFFIX + NamingUtils.underScoreToCamel(mElement.getName(), true);
	}

	private CodeBlock getDuringDurationPredicateBodyCodeBlock() {
		final CodeBlock now = SystemClassDescriptor.currentTimeMillis()
		                                           .build();
		final CodeBlock configFirstLoadingTime = getConfigSourceClassDescriptor().getFirstLoadTimeMillis()
		                                                                         .build();
		final CodeBlock timeSinceFirstFetch = new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_MINUS, now, configFirstLoadingTime)
		                                                            .build();
		final CodeBlock getterCall = new CodeBlockBuilder().addGeneratedMethodCall(mElement.getProviderMethod())
		                                                   .build();
		return new CodeBlockBuilder().addReturn(new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_GREATER_THAN, getterCall, timeSinceFirstFetch)
		                                                              .build())
		                             .build();
	}

	@Override
	protected CodeBlock getDefaultValueExpression() {
		return TimeUnitClassDescriptor.newInstance(mElement.getDefaultValueTimeUnit())
		                              .toMillis(mElement.getDefaultValue())
		                              .build();
	}
}