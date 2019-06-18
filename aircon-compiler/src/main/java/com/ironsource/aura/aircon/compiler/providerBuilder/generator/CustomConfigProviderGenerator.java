package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.AirConUtilsClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ConfigTypeResolverClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.CustomConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.CodeBlock;

/**
 * Created on 11/7/2018.
 */
class CustomConfigProviderGenerator
		extends DefaultConfigProviderGenerator<CustomConfigElement> {

	private static final String VAR_ANNOTATION = "annotation";

	CustomConfigProviderGenerator(final CustomConfigElement configElement) {
		super(configElement);
	}

	@Override
	protected CodeBlock getGetterBodyCodeBlock() {
		final CodeBlockBuilder builder = new CodeBlockBuilder();
		final CodeBlock annotationClass = ClassDescriptor.clazz(mElement.getAnnotation())
		                                                 .build();
		final CodeBlock configClass = ClassDescriptor.clazz(mElement.getConfigClassTypeName())
		                                             .build();
		final CodeBlock annotation = AirConUtilsClassDescriptor.getCustomConfigAnnotation(configClass, annotationClass, "\"" + mElement.getName() + "\"")
		                                                       .build();
		builder.addLocalVariable(mElement.getAnnotation(), VAR_ANNOTATION, annotation, true);
		builder.add(super.getGetterBodyCodeBlock());
		return builder.build();
	}

	@Override
	protected CodeBlock getValidationCondition(final String varValue) {
		return getConfigTypeResolver().isValid(VAR_ANNOTATION, varValue)
		                              .build();
	}

	@Override
	protected CodeBlock getConversionToTypeExpression(final Object varDefaultValue, final Object varValue) {
		return getConfigTypeResolver().process(VAR_ANNOTATION, varValue)
		                              .build();
	}

	private ConfigTypeResolverClassDescriptor getConfigTypeResolver() {
		final CodeBlock annotationClass = ClassDescriptor.clazz(mElement.getAnnotation())
		                                                 .build();
		return AirConClassDescriptor.get()
		                            .getConfigTypeResolver(mElement.getAnnotation(), mElement.getRawType(), mElement.getType(), annotationClass);
	}
}