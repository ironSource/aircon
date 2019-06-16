package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.ConfigType;
import com.ironsource.aura.aircon.common.IdentifiableConfigSource;
import com.ironsource.aura.aircon.common.RangeFallbackPolicy;
import com.ironsource.aura.aircon.common.annotations.DefaultConfig;
import com.ironsource.aura.aircon.common.annotations.DefaultRes;
import com.ironsource.aura.aircon.common.annotations.Mutable;
import com.ironsource.aura.aircon.common.annotations.Source;
import com.ironsource.aura.aircon.common.annotations.config.Configs;
import com.ironsource.aura.aircon.common.utils.Consts;
import com.ironsource.aura.aircon.compiler.model.annotation.ConfigAnnotationParser;
import com.ironsource.aura.aircon.compiler.model.annotation.ConfigKind;
import com.ironsource.aura.aircon.compiler.model.annotation.CustomConfigAnnotationParser;
import com.ironsource.aura.aircon.compiler.model.annotation.LibraryConfigAnnotationParser;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created on 11/4/2018.
 */
class ConfigFieldParser {

	private static final String ATTRIBUTE_DEFAULT_VALUE             = "defaultValue";
	private static final String ATTRIBUTE_ENFORCE_NON_EMPTY         = "enforceNonEmpty";
	private static final String ATTRIBUTE_MIN_VALUE                 = "minValue";
	private static final String ATTRIBUTE_MAX_VALUE                 = "maxValue";
	private static final String ATTRIBUTE_MIN_VALUE_FALLBACK_POLICY = "minValueFallbackPolicy";
	private static final String ATTRIBUTE_VALUE_FALLBACK_POLICY     = "maxValueFallbackPolicy";
	private static final String ATTRIBUTE_DEFAULT_VALUE_TIME_UNIT   = "defaultValueTimeUnit";
	private static final String ATTRIBUTE_RANDOMIZER_VALUE          = "randomizerValue";

	private static final int NO_RANDOMIZER_VALUE = Integer.MAX_VALUE;


	private final VariableElement mElement;
	private final ConfigKind      mConfigKind;

	private ConfigAnnotationParser mConfigAnnotation;

	private final Types mTypes;

	ConfigFieldParser(VariableElement element, final Types types) {
		mElement = element;
		mTypes = types;

		extractConfigAnnotation(element);

		mConfigKind = mConfigAnnotation != null ? mConfigAnnotation.getKind() : null;
	}

	ConfigKind getConfigKind() {
		return mConfigKind;
	}

	private void extractConfigAnnotation(final VariableElement element) {
		for (Class<? extends Annotation> configAnnotationClass : Configs.ALL) {
			final Annotation annotation = element.getAnnotation(configAnnotationClass);
			if (annotation != null) {
				mConfigAnnotation = new LibraryConfigAnnotationParser(annotation);
				return;
			}
		}

		extractCustomConfigTypeAnnotation(element);
	}

	private void extractCustomConfigTypeAnnotation(final VariableElement element) {
		final List<? extends AnnotationMirror> elementAnnotations = element.getAnnotationMirrors();
		for (AnnotationMirror annotationMirror : elementAnnotations) {
			if (hasConfigTypeAnnotation(annotationMirror)) {
				mConfigAnnotation = new CustomConfigAnnotationParser(mTypes, annotationMirror);
			}
		}
	}

	TypeName getAnnotationType() {
		return mConfigAnnotation.getAnnotationType();
	}

	private boolean hasConfigTypeAnnotation(final AnnotationMirror mirror) {
		return getConfigTypeAnnotation(mirror) != null;
	}

	private ConfigType getConfigTypeAnnotation(final AnnotationMirror mirror) {
		return mirror.getAnnotationType()
		             .asElement()
		             .getAnnotation(ConfigType.class);
	}

	boolean isConfigField() {
		return mConfigAnnotation != null && mElement.getConstantValue() instanceof String;
	}

	Object getDefaultValue() {
		Object defaultValue = mConfigAnnotation.getAttribute(ATTRIBUTE_DEFAULT_VALUE);
		if (defaultValue instanceof String && isNullString((String) defaultValue)) {
			defaultValue = null;
		}

		return defaultValue;
	}

	String getDefaultValueConfig() {
		final DefaultConfig annotation = mElement.getAnnotation(DefaultConfig.class);
		return annotation != null ? annotation.value() : null;
	}

	int getDefaultValueResId() {
		final DefaultRes annotation = mElement.getAnnotation(DefaultRes.class);
		return annotation != null ? annotation.value() : 0;
	}

	@SuppressWarnings("ConstantConditions")
	boolean isMutable() {
		return mElement.getAnnotation(Mutable.class) != null;
	}

	TypeMirror getSourceTypeMirror(Element configClass) {
		final TypeMirror typeMirror = getTypeMirror(mElement);
		return typeMirror != null ? typeMirror : getTypeMirror(configClass);
	}

	private TypeMirror getTypeMirror(final Element element) {
		final Source annotation = element.getAnnotation(Source.class);
		if (annotation != null) {
			try {
				annotation.value();
			} catch (MirroredTypeException e) {
				return e.getTypeMirror();
			}
		}

		return null;
	}

	TypeName getType() {
		return mConfigAnnotation.getType();
	}

	TypeName getRawType() {
		return mConfigAnnotation.getRawType();
	}

	@SuppressWarnings("ConstantConditions")
	boolean isEnforceNonEmpty() {
		final Boolean attribute = mConfigAnnotation.getAttribute(ATTRIBUTE_ENFORCE_NON_EMPTY);
		return attribute != null && attribute;
	}

	TypeMirror getEnumClass() {
		return mConfigAnnotation instanceof LibraryConfigAnnotationParser ? ((LibraryConfigAnnotationParser) mConfigAnnotation).getEnumClass() : null;
	}

	Number getMinValue() {
		final Number minValue = mConfigAnnotation.getAttribute(ATTRIBUTE_MIN_VALUE);
		return isValidLimitValue(minValue) ? minValue : null;
	}

	RangeFallbackPolicy getMinValueFallbackPolicy() {
		return mConfigAnnotation.getAttribute(ATTRIBUTE_MIN_VALUE_FALLBACK_POLICY);
	}

	RangeFallbackPolicy getMaxValueFallbackPolicy() {
		return mConfigAnnotation.getAttribute(ATTRIBUTE_VALUE_FALLBACK_POLICY);
	}

	Number getMaxValue() {
		final Number maxValue = mConfigAnnotation.getAttribute(ATTRIBUTE_MAX_VALUE);
		return isValidLimitValue(maxValue) ? maxValue : null;
	}

	TimeUnit getDefaultValueTimeUnit() {
		return mConfigAnnotation.getAttribute(ATTRIBUTE_DEFAULT_VALUE_TIME_UNIT);
	}

	Integer getRandomizerValue() {
		final Integer randomizerValue = mConfigAnnotation.getAttribute(ATTRIBUTE_RANDOMIZER_VALUE);
		return randomizerValue != null && randomizerValue != NO_RANDOMIZER_VALUE ? randomizerValue : null;
	}

	TypeMirror getSourceIdentifierTypeMirror(final TypeMirror sourceTypeMirror, final Elements elementsUtils, final Types types) {
		final TypeMirror identifiableConfigSourceTypeMirror = getIdentifiableConfigSourceTypeMirror(types, sourceTypeMirror);
		if (identifiableConfigSourceTypeMirror != null) {
			final List<? extends TypeMirror> typeArguments = ((DeclaredType) identifiableConfigSourceTypeMirror).getTypeArguments();
			return !typeArguments.isEmpty() ? typeArguments.get(0) : elementsUtils.getTypeElement(Object.class.getCanonicalName())
			                                                                      .asType();
		}

		return null;
	}

	private TypeMirror getIdentifiableConfigSourceTypeMirror(Types types, TypeMirror typeMirror) {
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

	private boolean isIdentifiableConfigSource(final TypeMirror typeMirror) {
		final String superTypeName = typeMirror.toString();
		return superTypeName.startsWith(IdentifiableConfigSource.class.getName());
	}

	private boolean isValidLimitValue(final Number minValue) {
		if (minValue instanceof Long && (minValue.longValue() == Long.MAX_VALUE || minValue.longValue() == Long.MIN_VALUE)) {
			return false;
		}

		if (minValue instanceof Integer && (minValue.intValue() == Integer.MAX_VALUE || minValue.intValue() == Integer.MIN_VALUE)) {
			return false;
		}

		return !(minValue instanceof Float) || (minValue.floatValue() != Float.MAX_VALUE && minValue.floatValue() != Float.MIN_VALUE);
	}

	private boolean isNullString(final String str) {
		return Consts.NULL_STRING.equalsIgnoreCase(str);
	}
}
