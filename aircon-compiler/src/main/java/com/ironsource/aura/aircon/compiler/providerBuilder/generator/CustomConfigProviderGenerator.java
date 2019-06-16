package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ConfigTypeResolverClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.CustomConfigElement;
import com.squareup.javapoet.CodeBlock;

/**
 * Created on 11/7/2018.
 */
class CustomConfigProviderGenerator
		extends DefaultConfigProviderGenerator<CustomConfigElement> {

	CustomConfigProviderGenerator(final CustomConfigElement configElement) {
		super(configElement);
	}

	@Override
	protected CodeBlock getValidationCondition(final String varValue) {
		return getConfigTypeResolver().isValid(varValue)
		                              .build();
	}

	@Override
	protected CodeBlock getConversionToTypeExpression(final Object varDefaultValue, final Object varValue) {
		return getConfigTypeResolver().process(varValue)
		                              .build();
	}

	private ConfigTypeResolverClassDescriptor getConfigTypeResolver() {
		final CodeBlock annotationClass = ClassDescriptor.clazz(mElement.getAnnotation())
		                                                 .build();
		return AirConClassDescriptor.get()
		                            .getConfigTypeResolver(mElement.getRawType(), mElement.getType(), annotationClass);
	}
}