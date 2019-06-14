package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.AirConUtilsClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.TypeTokenClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.JsonConfigElement;
import com.squareup.javapoet.CodeBlock;

/**
 * Created on 11/7/2018.
 */
class JsonConfigProviderGenerator
		extends DefaultConfigProviderGenerator<JsonConfigElement> {

	JsonConfigProviderGenerator(final JsonConfigElement configElement) {
		super(configElement);
	}

	@Override
	public CodeBlock getConversionToTypeExpression(final Object varDefaultValue, final Object varValue) {
		final CodeBlock clazz = getJsonClassCodeBlock();
		return AirConUtilsClassDescriptor.fromJson(varValue, clazz, varDefaultValue, getJsonConverter())
		                                 .build();
	}

	private CodeBlock getJsonClassCodeBlock() {
		if (mElement.isGenericType()) {
			return TypeTokenClassDescriptor.newInstance(mElement.getJsonType())
			                               .getType()
			                               .build();
		}
		else {
			return ClassDescriptor.clazz(mElement.getJsonType())
			                      .build();
		}
	}

	@Override
	protected CodeBlock getConversionToRawTypeExpression(final Object value) {
		return AirConUtilsClassDescriptor.toJson(value, getJsonConverter())
		                                 .build();
	}

	private CodeBlock getJsonConverter() {
		return AirConClassDescriptor.get()
		                            .getJsonConverter()
		                            .build();
	}
}
