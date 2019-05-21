package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.ArraysClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.HashSetClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.StringSetConfigElement;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

/**
 * Created on 11/7/2018.
 */
class StringSetConfigProviderGenerator
		extends DefaultConfigProviderGenerator<StringSetConfigElement> {

	private static final String SETTING_CLASS_NAME = "StringSet";

	StringSetConfigProviderGenerator(final StringSetConfigElement configElement) {
		super(configElement);
	}

	@Override
	protected String getConfigTypeStr() {
		return SETTING_CLASS_NAME;
	}

	@Override
	protected CodeBlock getDefaultValueExpression() {
		final TypeName typeName = TypeName.get(String.class);
		final String[] defaultValue = (String[]) mElement.getDefaultValue();
		if (defaultValue.length == 0) {
			return HashSetClassDescriptor.newInstance(typeName)
			                             .build();
		}
		else {
			String values = "";
			for (int i = 0 ; i < defaultValue.length ; i++) {
				values += String.format("\"%s\"", defaultValue[i]);
				if (i < defaultValue.length - 1) {
					values += ", ";
				}
			}
			final CodeBlock list = ArraysClassDescriptor.asList(values)
			                                            .build();
			return HashSetClassDescriptor.from(typeName, list)
			                             .build();
		}
	}
}