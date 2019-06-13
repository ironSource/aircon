package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.descriptors.AirConClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ConfigTypeResolverClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.CustomConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.AnnotationMirror;

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
		final AnnotationMirror annotationTypeMirror = mElement.getAnnotationTypeMirror();
		final CodeBlock annotationClass = new CodeBlockBuilder().addClassQualifier(TypeName.get(annotationTypeMirror.getAnnotationType()))
		                                                        .add(CodeBlockBuilder.CLASS)
		                                                        .build();
		return AirConClassDescriptor.get()
		                            .getConfigTypeResolver(mElement.getRawType(), mElement.getType(), annotationClass);
	}
}