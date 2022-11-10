package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

import static com.ironsource.aura.aircon.compiler.consts.Consts.BASE_AIRCON_PACKAGE;

/**
 * Created on 11/13/2018.
 */
public class AirConUtilsClassDescriptor
		extends ClassDescriptor {

	private static final String PACKAGE = BASE_AIRCON_PACKAGE + ".utils";

	public static final ClassName CLASS_NAME = ClassName.get(PACKAGE, "AirConUtils");

	private interface StaticMethods {

		String IS_VALID_URL                 = "isValidUrl";
		String HEX_TO_COLOR                 = "hexToColorInt";
		String COLOR_TO_HEX                 = "colorIntToHex";
		String GET_COLOR_RES_AS_HEX         = "getColorResAsHex";
		String TO_JSON                      = "toJson";
		String FROM_JSON                    = "fromJson";
		String GET_RANDOM_ENUM_VALUE        = "getRandomEnumValue";
		String GET_CUSTOM_CONFIG_ANNOTATION = "getCustomConfigAnnotation";
	}

	private AirConUtilsClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static StubClassDescriptor isValidUrl(Object url) {
		return staticMethod(StaticMethods.IS_VALID_URL, url);
	}

	public static StubClassDescriptor hexToColorInt(Object varValue, Object hex) {
		return staticMethod(StaticMethods.HEX_TO_COLOR, hex, varValue);
	}

	public static StubClassDescriptor colorIntToHex(Object color) {
		return staticMethod(StaticMethods.COLOR_TO_HEX, color);
	}

	public static StubClassDescriptor getColorResAsHex(Object resources, Object color) {
		return staticMethod(StaticMethods.GET_COLOR_RES_AS_HEX, resources, color);
	}

	public static StubClassDescriptor toJson(Object obj, Object jsonConverter) {
		return staticMethod(StaticMethods.TO_JSON, obj, jsonConverter);
	}

	public static StubClassDescriptor fromJson(Object json, Object clazz, Object defaultValue, Object jsonConverter) {
		return staticMethod(StaticMethods.FROM_JSON, json, clazz, defaultValue, jsonConverter);
	}

	public static StubClassDescriptor getRandomEnumValue(Object configKey, Object enumClass) {
		return staticMethod(StaticMethods.GET_RANDOM_ENUM_VALUE, configKey, enumClass);
	}

	public static StubClassDescriptor getCustomConfigAnnotation(Object featureClass, Object annotationClass, Object fieldName) {
		return staticMethod(StaticMethods.GET_CUSTOM_CONFIG_ANNOTATION, featureClass, annotationClass, fieldName);
	}

	private static StubClassDescriptor staticMethod(String method, Object... params) {
		return new StubClassDescriptor(staticMethodCall(CLASS_NAME, method, params));
	}
}