package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;
import com.ironsource.aura.aircon.compiler.model.ConfigAuxMethod;
import com.ironsource.aura.aircon.compiler.model.annotation.ConfigKind;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
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
		final String key = (String) element.getConstantValue();
		final String javadoc = elementsUtils.getDocComment(element);

		final ConfigKind configKind = parser.getConfigKind();
		final TypeName rawType = parser.getRawType();
		final TypeName type = parser.getType();

		Object defaultValue = parser.getDefaultValue();
		final TypeMirror sourceTypeMirror = parser.getSourceTypeMirror(configClass);
		final TypeMirror sourceIdentifierTypeMirror = parser.getSourceIdentifierTypeMirror(sourceTypeMirror, elementsUtils, types);
		final boolean mutable = parser.isMutable();

		final Number minValue = parser.getMinValue();
		final Number maxValue = parser.getMaxValue();
		final RangeFallbackPolicy minValueFallbackPolicy = parser.getMinValueFallbackPolicy();
		final RangeFallbackPolicy maxValueFallbackPolicy = parser.getMaxValueFallbackPolicy();

		final ConfigAuxMethod defaultValueProvider = ConfigAuxMethod.from(defaultValueProviders.get(key));
		final ConfigAuxMethod adapter = ConfigAuxMethod.from(adapters.get(key));
		final ConfigAuxMethod validator = ConfigAuxMethod.from(validators.get(key));
		final ConfigAuxMethod mock = ConfigAuxMethod.from(mocks.get(key));

		final ConfigElement.Properties properties = new ConfigElement.Properties(name, key, providerClassName, javadoc, sourceTypeMirror, sourceIdentifierTypeMirror, type, rawType, defaultValue, parser.getDefaultValueConfig(), parser.getDefaultValueResId(), defaultValueProvider, mutable, adapter, validator, mock);

		switch (configKind) {
			case TIME:
				return new TimeConfigElement(properties, minValue, maxValue, minValueFallbackPolicy, maxValueFallbackPolicy, parser.getDefaultValueTimeUnit());
			case ENUM:
				return new EnumConfigElement(properties, parser.getEnumClass(), parser.getRandomizerValue());
			case JSON:
				return new JsonConfigElement(properties);
			case COLOR:
				return new ColorConfigElement(properties);
			case STRING:
				return new StringConfigElement(properties, parser.isEnforceNonEmpty());
			case URL:
				return new UrlConfigElement(properties, parser.isEnforceNonEmpty());
			case TEXT:
				return new TextConfigElement(properties, parser.isEnforceNonEmpty());
			case STRING_SET:
				return new StringSetConfigElement(properties);
			case NUMBER:
				return new NumberConfigElement(properties, minValue, maxValue, minValueFallbackPolicy, maxValueFallbackPolicy);
			case CUSTOM:
				return new CustomConfigElement(properties, parser.getAnnotationType());
			case PRIMITIVE:
			default:
				return new PrimitiveConfigElement(properties);
		}
	}
}
