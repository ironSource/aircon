package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.AirConUtilsClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.JsonConfigElement;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

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
		final CodeBlock clazz = ClassDescriptor.clazz(TypeName.get(mElement.getJsonType()))
		                                       .build();
		return AirConUtilsClassDescriptor.fromJson(varValue, clazz, varDefaultValue, getJsonConverter())
		                                 .build();
	}

	@Override
	protected CodeBlock getConversionToRawTypeExpression(final Object value) {
		return AirConUtilsClassDescriptor.toJson(value, getJsonConverter())
		                                 .build();
	}

	private CodeBlock getJsonConverter() {
		return AirConClassDescriptor.get()
		                            .getJsonConverter().build();
	}
}
