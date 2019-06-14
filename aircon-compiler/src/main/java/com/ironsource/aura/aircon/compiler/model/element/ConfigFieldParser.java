package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;
import com.ironsource.aura.aircon.common.annotations.DefaultConfig;
import com.ironsource.aura.aircon.common.annotations.DefaultRes;
import com.ironsource.aura.aircon.common.annotations.Mutable;
import com.ironsource.aura.aircon.common.annotations.Source;
import com.ironsource.aura.aircon.common.annotations.config.ColorConfig;
import com.ironsource.aura.aircon.common.annotations.config.Configs;
import com.ironsource.aura.aircon.common.annotations.config.FloatConfig;
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
import com.ironsource.aura.aircon.common.utils.Consts;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/4/2018.
 */
public class ConfigFieldParser {

	private static final String METHOD_DEFAULT_VALUE             = "defaultValue";
	private static final String METHOD_ENFORCE_NON_EMPTY         = "enforceNonEmpty";
	private static final String METHOD_MIN_VALUE                 = "minValue";
	private static final String METHOD_MAX_VALUE                 = "maxValue";
	private static final String METHOD_MIN_VALUE_FALLBACK_POLICY = "minValueFallbackPolicy";
	private static final String METHOD_MAX_VALUE_FALLBACK_POLICY = "maxValueFallbackPolicy";
	private static final String RANDOMIZER_VALUE                 = "randomizerValue";

	private static final int NO_RANDOMIZER_VALUE = Integer.MAX_VALUE;

	private       Annotation      mConfigAnnotation;
	private final VariableElement mElement;

	public ConfigFieldParser(final Annotation configAnnotation, final VariableElement element) {
		mConfigAnnotation = configAnnotation;
		mElement = element;
	}

	public ConfigFieldParser(VariableElement element) {
		mElement = element;

		for (Class<? extends Annotation> configAnnotationClass : Configs.ALL) {
			if ((mConfigAnnotation = element.getAnnotation(configAnnotationClass)) != null) {
				break;
			}
		}
	}

	public boolean isConfigField() {
		return mConfigAnnotation != null;
	}

	public Object getDefaultValue() {
		Object defaultValue = getAttribute(METHOD_DEFAULT_VALUE);
		if (defaultValue instanceof String && isNullString((String) defaultValue)) {
			defaultValue = null;
		}
		return defaultValue;
	}

	public String getDefaultValueConfig() {
		final DefaultConfig annotation = mElement.getAnnotation(DefaultConfig.class);
		return annotation != null ? annotation.value() : null;
	}

	public int getDefaultValueResId() {
		final DefaultRes annotation = mElement.getAnnotation(DefaultRes.class);
		return annotation != null ? annotation.value() : 0;
	}

	@SuppressWarnings("ConstantConditions")
	public boolean isMutable() {
		return mElement.getAnnotation(Mutable.class) != null;
	}

	public TypeMirror getSourceTypeMirror(Element configClass) {
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


	@SuppressWarnings("ConstantConditions")
	public boolean isEnforceNonEmpty() {
		final Boolean attribute = getAttribute(METHOD_ENFORCE_NON_EMPTY);
		return attribute != null && attribute;
	}

	public Number getMinValue() {
		return getAttribute(METHOD_MIN_VALUE);
	}

	public RangeFallbackPolicy getMinValueFallbackPolicy() {
		return getAttribute(METHOD_MIN_VALUE_FALLBACK_POLICY);
	}

	public RangeFallbackPolicy getMaxValueFallbackPolicy() {
		return getAttribute(METHOD_MAX_VALUE_FALLBACK_POLICY);
	}

	public Number getMaxValue() {
		return getAttribute(METHOD_MAX_VALUE);
	}

	public TypeName getType() {
		return TypeName.get(Objects.requireNonNull(getAttribute(METHOD_DEFAULT_VALUE))
		                           .getClass());
	}

	public boolean isTime() {
		return mConfigAnnotation instanceof TimeConfig;
	}

	public TimeUnit getDefaultValueTimeUnit() {
		return ((TimeConfig) Objects.requireNonNull(mConfigAnnotation)).defaultValueTimeUnit();
	}

	public boolean isEnum() {
		return isIntEnum() || isStringEnum();
	}

	public boolean isIntEnum() {
		return mConfigAnnotation instanceof IntEnumConfig;
	}

	public boolean isStringEnum() {
		return mConfigAnnotation instanceof StringEnumConfig;
	}

	public TypeMirror getEnumClass() {
		try {
			if (isIntEnum()) {
				((IntEnumConfig) mConfigAnnotation).enumClass();
			}
			else {
				((StringEnumConfig) mConfigAnnotation).enumClass();
			}
		} catch (MirroredTypeException e) {
			return e.getTypeMirror();
		}
		// Should never happen
		return null;
	}

	public boolean isJson() {
		return mConfigAnnotation instanceof JsonConfig;
	}

	public TypeMirror getJsonType() {
		try {
			((JsonConfig) mConfigAnnotation).type();
		} catch (MirroredTypeException e) {
			return e.getTypeMirror();
		}

		return null;
	}

	public List<? extends TypeMirror> getJsonGenericTypes() {
		try {
			((JsonConfig) mConfigAnnotation).genericTypes();
		} catch (MirroredTypesException e) {
			return e.getTypeMirrors();
		}

		return null;
	}

	public Integer getRandomizerValue() {
		Integer randomizerValue = null;
		if (isIntEnum()) {
			randomizerValue = ((IntEnumConfig) mConfigAnnotation).randomizerValue();
		}
		else if (isStringEnum()) {
			randomizerValue = ((StringEnumConfig) mConfigAnnotation).randomizerValue();
		}

		return randomizerValue != null && randomizerValue != NO_RANDOMIZER_VALUE ? randomizerValue : null;
	}

	public boolean isColor() {
		return mConfigAnnotation instanceof ColorConfig;
	}

	public boolean isString() {
		return mConfigAnnotation instanceof StringConfig;
	}

	public boolean isUrl() {
		return mConfigAnnotation instanceof UrlConfig;
	}

	public boolean isText() {
		return mConfigAnnotation instanceof TextConfig;
	}

	public boolean isStringSet() {
		return mConfigAnnotation instanceof StringSetConfig;
	}

	public boolean isNumber() {
		return mConfigAnnotation instanceof LongConfig || mConfigAnnotation instanceof IntConfig || mConfigAnnotation instanceof FloatConfig;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttribute(String attributeName) {
		try {
			return (T) Objects.requireNonNull(mConfigAnnotation)
			                  .annotationType()
			                  .getMethod(attributeName)
			                  .invoke(mConfigAnnotation);
		} catch (Exception e) {
		}

		return null;
	}

	private boolean isNullString(final String str) {
		return Consts.NULL_STRING.equalsIgnoreCase(str);
	}
}
