package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConUtilsClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ResourcesClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.ColorConfigElement;
import com.squareup.javapoet.CodeBlock;

/**
 * Created on 11/7/2018.
 */
class ColorConfigProviderGenerator
		extends DefaultConfigProviderGenerator<ColorConfigElement> {

	ColorConfigProviderGenerator(final ColorConfigElement configElement) {
		super(configElement);
	}

	@Override
	protected CodeBlock getResId(final ResourcesClassDescriptor resourcesClassDescriptor, final int resId) {
		return AirConUtilsClassDescriptor.getColorResAsHex(resourcesClassDescriptor.build(), resId)
		                                 .build();
	}

	@Override
	public CodeBlock getConversionToTypeExpression(final Object varDefaultValue, final Object varValue) {
		return AirConUtilsClassDescriptor.hexToColorInt(varDefaultValue, varValue)
		                                 .build();
	}

	@Override
	protected CodeBlock getConversionToRawTypeExpression(final Object value) {
		return AirConUtilsClassDescriptor.colorIntToHex(value)
		                                 .build();
	}
}