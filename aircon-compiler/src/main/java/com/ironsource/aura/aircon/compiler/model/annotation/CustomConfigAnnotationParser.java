package com.ironsource.aura.aircon.compiler.model.annotation;

import com.ironsource.aura.aircon.common.ConfigType;
import com.ironsource.aura.aircon.common.ConfigTypeResolver;
import com.ironsource.aura.aircon.compiler.utils.Utils;
import com.squareup.javapoet.TypeName;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

public class CustomConfigAnnotationParser
		implements ConfigAnnotationParser {

	private static final int GENERIC_INDEX_RAW_TYPE = 0;
	private static final int GENERIC_INDEX_TYPE     = 1;

	private final Types mTypes;

	private final AnnotationMirror mAnnotationMirror;

	private List<TypeMirror> mConfigTypeGenericTypes;

	public CustomConfigAnnotationParser(Types types, @NonNull final AnnotationMirror annotationMirror) {
		mTypes = types;
		mAnnotationMirror = annotationMirror;
	}

	@Override
	public TypeName getAnnotationType() {
		return TypeName.get(mAnnotationMirror.getAnnotationType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String attr) {
		T value = getAttributeFromElementValues(attr);
		if (value == null) {
			value = getAttributeFromAnnotationClass(attr);
		}

		return value;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttributeFromElementValues(String attr) {
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mAnnotationMirror.getElementValues()
		                                                                                                .entrySet()) {
			if (entry.getKey()
			         .getSimpleName()
			         .toString()
			         .equals(attr)) {
				return (T) entry.getValue()
				                .getValue();
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttributeFromAnnotationClass(String attr) {
		final TypeElement annotationElement = (TypeElement) mAnnotationMirror.getAnnotationType()
		                                                                     .asElement();

		for (final Element enclosedElement : annotationElement.getEnclosedElements()) {
			if (enclosedElement instanceof ExecutableElement) {
				final ExecutableElement method = (ExecutableElement) enclosedElement;
				if (method.getSimpleName()
				          .toString()
				          .equals(attr)) {
					return (T) method.getDefaultValue()
					                 .getValue();
				}
			}
		}

		return null;
	}

	@Override
	public TypeName getRawType() {
		return TypeName.get(getCustomConfigTypeResolverGenerics().get(GENERIC_INDEX_RAW_TYPE));
	}

	private List<TypeMirror> getCustomConfigTypeResolverGenerics() {
		if (mConfigTypeGenericTypes != null) {
			return mConfigTypeGenericTypes;
		}

		final ConfigType configTypeAnnotation = getConfigTypeAnnotation();
		final TypeMirror configTypeResolverTypeMirror = getConfigTypeResolverTypeMirror(configTypeAnnotation);

		final TypeMirror configTypeInterfaceMirror = Utils.findSuperInterface(mTypes, configTypeResolverTypeMirror, ConfigTypeResolver.class);

		mConfigTypeGenericTypes = configTypeInterfaceMirror != null ? Utils.getGenericTypes(configTypeInterfaceMirror) : new ArrayList<TypeMirror>();

		return mConfigTypeGenericTypes;
	}

	private ConfigType getConfigTypeAnnotation() {
		return mAnnotationMirror.getAnnotationType()
		                        .asElement()
		                        .getAnnotation(ConfigType.class);
	}

	private TypeMirror getConfigTypeResolverTypeMirror(final ConfigType configTypeAnnotation) {
		try {
			configTypeAnnotation.value();
		} catch (MirroredTypeException e) {
			return e.getTypeMirror();
		}
		// Should never happen
		return null;
	}

	@Override
	public TypeName getType() {
		return TypeName.get(getCustomConfigTypeResolverGenerics().get(GENERIC_INDEX_TYPE));
	}

	@Override
	public ConfigKind getKind() {
		return ConfigKind.CUSTOM;
	}
}
