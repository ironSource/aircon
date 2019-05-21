package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConUtilsClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.UrlConfigElement;
import com.squareup.javapoet.CodeBlock;

/**
 * Created on 11/7/2018.
 */
class UrlConfigProviderGenerator
		extends StringConfigProviderGenerator<UrlConfigElement> {

	UrlConfigProviderGenerator(final UrlConfigElement configElement) {
		super(configElement);
	}

	@Override
	protected CodeBlock getValidationCondition(final String varValue) {
		return AirConUtilsClassDescriptor.isValidUrl(varValue)
		                                 .build();
	}
}