package com.ironsource.aura.aircon.compiler.model.annotation;

import com.ironsource.aura.aircon.common.annotations.config.ColorConfig;
import com.ironsource.aura.aircon.common.annotations.config.FloatConfig;
import com.ironsource.aura.aircon.common.annotations.config.HtmlConfig;
import com.ironsource.aura.aircon.common.annotations.config.IntConfig;
import com.ironsource.aura.aircon.common.annotations.config.IntEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.JsonConfig;
import com.ironsource.aura.aircon.common.annotations.config.LongConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringSetConfig;
import com.ironsource.aura.aircon.common.annotations.config.TextConfig;
import com.ironsource.aura.aircon.common.annotations.config.TimeConfig;
import com.ironsource.aura.aircon.common.annotations.config.UrlConfig;
import com.ironsource.aura.aircon.compiler.descriptors.ColorIntClassDescriptor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

public class LibraryConfigAnnotationParser
		implements ConfigAnnotationParser {

	private static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValue";
	private static final String ATTRIBUTE_ENUM_CLASS    = "enumClass";

	private Annotation mConfigAnnotation;

	public LibraryConfigAnnotationParser(final Annotation configAnnotation) {
		mConfigAnnotation = configAnnotation;
	}

	@Override
	public ConfigKind getKind() {
		if (mConfigAnnotation instanceof TimeConfig) {
			return ConfigKind.TIME;
		}
		if (mConfigAnnotation instanceof StringConfig) {
			return ConfigKind.STRING;
		}
		if (mConfigAnnotation instanceof UrlConfig) {
			return ConfigKind.URL;
		}
		if (mConfigAnnotation instanceof TextConfig) {
			return ConfigKind.TEXT;
		}
		if (mConfigAnnotation instanceof LongConfig || mConfigAnnotation instanceof IntConfig || mConfigAnnotation instanceof FloatConfig) {
			return ConfigKind.NUMBER;
		}
		if (mConfigAnnotation instanceof IntEnumConfig || mConfigAnnotation instanceof StringEnumConfig) {
			return ConfigKind.ENUM;
		}
		if (mConfigAnnotation instanceof JsonConfig) {
			return ConfigKind.JSON;
		}
		if (mConfigAnnotation instanceof ColorConfig) {
			return ConfigKind.COLOR;
		}
		if (mConfigAnnotation instanceof StringSetConfig) {
			return ConfigKind.STRING_SET;
		}
		if (mConfigAnnotation instanceof HtmlConfig) {
			return ConfigKind.HTML;
		}

		return ConfigKind.PRIMITIVE;
	}

	@Override
	public TypeName getAnnotationType() {
		return TypeName.get(mConfigAnnotation.getClass());
	}

	@Override
	public TypeName getRawType() {
		return getType(true);
	}

	@Override
	public TypeName getType() {
		return getType(false);
	}

	private TypeName getType(boolean raw) {
		final ConfigKind kind = getKind();
		if (kind == ConfigKind.STRING_SET) {
			return ParameterizedTypeName.get(ClassName.get(Set.class), TypeVariableName.get(String.class));
		}

		final TypeName rawType = TypeName.get(Objects.requireNonNull(getAttribute(ATTRIBUTE_DEFAULT_VALUE))
		                                             .getClass());

		if (raw) {
			return rawType;
		}

		if (kind == ConfigKind.ENUM) {
			return TypeName.get(getEnumClass());
		}
		else if (kind == ConfigKind.JSON) {
			return getJsonType();
		}
		else if (kind == ConfigKind.COLOR) {
			return ColorIntClassDescriptor.CLASS_NAME;
		}

		return rawType;
	}

	public TypeMirror getEnumClass() {
		return getAttribute(ATTRIBUTE_ENUM_CLASS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(final String attr) {
		try {
			return (T) Objects.requireNonNull(mConfigAnnotation)
			                  .annotationType()
			                  .getMethod(attr)
			                  .invoke(mConfigAnnotation);
		} catch (Exception e) {
			if (e.getCause() instanceof MirroredTypeException) {
				return (T) ((MirroredTypeException) e.getCause()).getTypeMirror();
			}
		}

		return null;
	}

	private TypeName getJsonType() {
		final TypeMirror jsonType = getAnnotationJsonType();
		final List<? extends TypeMirror> jsonGenericTypes = getJsonGenericTypes();
		if (jsonGenericTypes != null && !jsonGenericTypes.isEmpty()) {
			return ParameterizedTypeName.get(ClassName.bestGuess(jsonType.toString()), getGenericTypeNames(jsonGenericTypes));
		}
		else {
			return TypeName.get(jsonType);
		}
	}

	private TypeName[] getGenericTypeNames(final List<? extends TypeMirror> genericTypes) {
		final List<TypeName> genericTypeNames = new ArrayList<>();
		for (TypeMirror genericType : genericTypes) {
			genericTypeNames.add(TypeName.get(genericType));
		}
		return genericTypeNames.toArray(new TypeName[0]);
	}

	private TypeMirror getAnnotationJsonType() {
		try {
			((JsonConfig) mConfigAnnotation).type();
		} catch (MirroredTypeException e) {
			return e.getTypeMirror();
		}

		return null;
	}

	private List<? extends TypeMirror> getJsonGenericTypes() {
		try {
			((JsonConfig) mConfigAnnotation).genericTypes();
		} catch (MirroredTypesException e) {
			return e.getTypeMirrors();
		}

		return null;
	}
}
