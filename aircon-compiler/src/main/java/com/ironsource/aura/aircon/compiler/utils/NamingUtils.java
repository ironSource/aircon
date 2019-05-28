package com.ironsource.aura.aircon.compiler.utils;

import com.ironsource.aura.aircon.common.utils.CommonNamingUtils;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;

/**
 * Created on 11/7/2018.
 */
public class NamingUtils
		extends CommonNamingUtils {

	public static final  String ENUMS_PROVIDER_CLASS_NAME                 = "EnumsProvider";
	public static final  String ENUMS_PROVIDER_REMOTE_VALUE_GETTER_METHOD = "getRemoteValue";
	private static final String FEATURE_CONFIG_GROUP_CLASS_SUFFIX         = "Config";

	public static final String PARAMETER_PRODUCT_FEED = "productFeed";
	public static final String PARAMETER_APP_FEED     = "appFeed";


	public static String getProviderClassName(TypeElement featureConfigInterface) {
		return getProviderClassName(featureConfigInterface.getSimpleName()
		                                                  .toString());
	}

	public static String getSimpleName(TypeName typeName) {
		final String[] arr = typeName.toString()
		                             .split("\\.");
		return arr[arr.length - 1];
	}

	public static String getFeatureConfigGroupName(final TypeElement configClass) {
		return configClass.getSimpleName() + FEATURE_CONFIG_GROUP_CLASS_SUFFIX;
	}
}
