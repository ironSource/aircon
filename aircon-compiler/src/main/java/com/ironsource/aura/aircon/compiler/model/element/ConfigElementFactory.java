package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.IdentifiableConfigSource;
import com.ironsource.aura.aircon.common.RangeFallbackPolicy;
import com.ironsource.aura.aircon.compiler.descriptors.ColorIntClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.ConfigAuxMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


/**
 * Created on 11/3/2018.
 */
public class ConfigElementFactory {

	public static ConfigElement create(final ClassName providerClassName, Element configClass, VariableElement element, Elements elementsUtils, final Types types, final Map<String, ExecutableElement> defaultValueProviders, final Map<String, ExecutableElement> adapters, final Map<String, ExecutableElement> validators, final Map<String, ExecutableElement> mocks) {
		final ConfigFieldParser parser = new ConfigFieldParser(element, types);

		if (!parser.isConfigField()) {
			return null;
		}

		final String name = element.getSimpleName()
		                           .toString()
		                           .toUpperCase();
		if (!String.class.isInstance(element.getConstantValue())) {
			return null;
		}

		final String key = (String) element.getConstantValue();
		final String javadoc = elementsUtils.getDocComment(element);

		final TypeName rawType = getType(parser, true);
		final TypeName type = getType(parser, false);

		Object defaultValue = parser.getDefaultValue();
		final TypeMirror sourceTypeMirror = parser.getSourceTypeMirror(configClass);
		final TypeMirror sourceIdentifierTypeMirror = getSourceIdentifierTypeMirror(sourceTypeMirror, elementsUtils, types);
		final boolean mutable = parser.isMutable();

		final Number minValue = getMinValue(parser);
		final Number maxValue = getMaxValue(parser);
		final RangeFallbackPolicy minValueFallbackPolicy = parser.getMinValueFallbackPolicy();
		final RangeFallbackPolicy maxValueFallbackPolicy = parser.getMaxValueFallbackPolicy();

		final ConfigAuxMethod defaultValueProvider = ConfigAuxMethod.from(defaultValueProviders.get(key));
		final ConfigAuxMethod adapter = ConfigAuxMethod.from(adapters.get(key));
		final ConfigAuxMethod validator = ConfigAuxMethod.from(validators.get(key));
		final ConfigAuxMethod mock = ConfigAuxMethod.from(mocks.get(key));

		final ConfigElement.Properties properties = new ConfigElement.Properties(name, key, providerClassName, javadoc, sourceTypeMirror, sourceIdentifierTypeMirror, type, rawType, defaultValue, parser.getDefaultValueConfig(), parser.getDefaultValueResId(), defaultValueProvider, mutable, adapter, validator, mock);

		if (parser.isTime()) {
			return new TimeConfigElement(properties, minValue, maxValue, minValueFallbackPolicy, maxValueFallbackPolicy, parser.getDefaultValueTimeUnit());
		}
		else if (parser.isEnum()) {
			return new EnumConfigElement(properties, parser.getEnumClass(), parser.getRandomizerValue());
		}
		else if (parser.isJson()) {
			return new JsonConfigElement(properties);
		}
		else if (parser.isColor()) {
			return new ColorConfigElement(properties);
		}
		else if (parser.isString()) {
			return new StringConfigElement(properties, parser.isEnforceNonEmpty());
		}
		else if (parser.isUrl()) {
			return new UrlConfigElement(properties, parser.isEnforceNonEmpty());
		}
		else if (parser.isText()) {
			return new TextConfigElement(properties, parser.isEnforceNonEmpty());
		}
		else if (parser.isStringSet()) {
			return new StringSetConfigElement(properties);
		}
		else if (parser.isNumber()) {
			return new NumberConfigElement(properties, minValue, maxValue, minValueFallbackPolicy, maxValueFallbackPolicy);
		}
		else if (parser.isCustomType()) {
			return new CustomConfigElement(properties, parser.getCustomConfigAnnotationMirror());
		}
		else {
			return new PrimitiveConfigElement(properties);
		}
	}

	private static TypeMirror getSourceIdentifierTypeMirror(final TypeMirror sourceTypeMirror, final Elements elementsUtils, final Types types) {
		final TypeMirror identifiableConfigSourceTypeMirror = getIdentifiableConfigSourceTypeMirror(types, sourceTypeMirror);
		if (identifiableConfigSourceTypeMirror != null) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) identifiableConfigSourceTypeMirror).getTypeArguments();
			return !typeArguments.isEmpty() ? typeArguments.get(0) : elementsUtils.getTypeElement(Object.class.getCanonicalName())
			                                                                      .asType();
		}

		return null;
	}

	private static TypeMirror getIdentifiableConfigSourceTypeMirror(Types types, TypeMirror typeMirror) {
		final Queue<TypeMirror> typeMirrors = new LinkedList<>(Collections.singleton(typeMirror));
		while (!typeMirrors.isEmpty()) {
			final TypeMirror item = typeMirrors.remove();
			if (isIdentifiableConfigSource(item)) {
				return item;
			}
			typeMirrors.addAll(types.directSupertypes(item));
		}

		return null;
	}

	private static boolean isIdentifiableConfigSource(final TypeMirror typeMirror) {
		final String superTypeName = typeMirror.toString();
		return superTypeName.startsWith(IdentifiableConfigSource.class.getName());
	}

	private static Number getMinValue(final ConfigFieldParser parser) {
		final Number minValue = parser.getMinValue();
		return isValidLimitValue(minValue) ? minValue : null;
	}

	private static Number getMaxValue(final ConfigFieldParser parser) {
		final Number maxValue = parser.getMaxValue();
		return isValidLimitValue(maxValue) ? maxValue : null;
	}

	private static boolean isValidLimitValue(final Number minValue) {
		if (minValue instanceof Long && (minValue.longValue() == Long.MAX_VALUE || minValue.longValue() == Long.MIN_VALUE)) {
			return false;
		}

		if (minValue instanceof Integer && (minValue.intValue() == Integer.MAX_VALUE || minValue.intValue() == Integer.MIN_VALUE)) {
			return false;
		}

		if (minValue instanceof Float && (minValue.floatValue() == Float.MAX_VALUE || minValue.floatValue() == Float.MIN_VALUE)) {
			return false;
		}
		return true;
	}

	private static TypeName getType(final ConfigFieldParser configFieldParser, boolean raw) {
		if (configFieldParser.isStringSet()) {
			return ParameterizedTypeName.get(ClassName.get(Set.class), TypeVariableName.get(String.class));
		}
		if (!raw) {
			if (configFieldParser.isEnum()) {
				return TypeName.get(configFieldParser.getEnumClass());
			}
			else if (configFieldParser.isJson()) {
				return getJsonType(configFieldParser.getJsonType(), configFieldParser.getJsonGenericTypes());
			}
			else if (configFieldParser.isColor()) {
				return ColorIntClassDescriptor.CLASS_NAME;
			}
			else if (configFieldParser.isCustomType()) {
				return configFieldParser.getCustomType();
			}
		}

		return configFieldParser.getType();
	}

	private static TypeName getJsonType(final TypeMirror jsonType, final List<? extends TypeMirror> jsonGenericTypes) {
		if (jsonGenericTypes != null && !jsonGenericTypes.isEmpty()) {
			return ParameterizedTypeName.get(ClassName.bestGuess(jsonType.toString()), getGenericTypeNames(jsonGenericTypes));
		}
		else {
			return TypeName.get(jsonType);
		}
	}

	private static TypeName[] getGenericTypeNames(final List<? extends TypeMirror> genericTypes) {
		final List<TypeName> genericTypeNames = new ArrayList<>();
		for (TypeMirror genericType : genericTypes) {
			genericTypeNames.add(TypeName.get(genericType));
		}
		return genericTypeNames.toArray(new TypeName[0]);
	}
}
