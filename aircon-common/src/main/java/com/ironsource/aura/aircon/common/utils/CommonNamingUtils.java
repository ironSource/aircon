package com.ironsource.aura.aircon.common.utils;

/**
 * Created on 11/8/2018.
 */
public class CommonNamingUtils {

	private static final String ACTION_METHOD_FLAG_ONLY_SUFFIX      = "IfEnabled";
	private static final String REMOTE_CONFIG_PROVIDER_CLASS_SUFFIX = "ConfigProvider";

	public static String getProviderClassName(String configInterfaceName) {
		return configInterfaceName + REMOTE_CONFIG_PROVIDER_CLASS_SUFFIX;
	}

	public static String getConfigGroupClassName(String groupKey) {
		return capitalizeFirstLetter(groupKey);
	}

	public static String getActionMethodName(String methodName, boolean onlyRemoteFlag) {
		return methodName + (onlyRemoteFlag ? ACTION_METHOD_FLAG_ONLY_SUFFIX : "");
	}

	public static String underScoreToCamel(String str, boolean capitalizeFirst) {
		final String[] strArr = str.split("_");

		StringBuilder builder = new StringBuilder();
		for (int i = 0 ; i < strArr.length ; i++) {
			final String fieldNameSegment = strArr[i].toLowerCase();
			final char firstChar = fieldNameSegment.charAt(0);
			builder.append(i != 0 || capitalizeFirst ? Character.toUpperCase(firstChar) : firstChar)
			       .append(fieldNameSegment, 1, fieldNameSegment.length());
		}

		return builder.toString();
	}

	public static boolean isStringUpperCase(String str) {
		for (final char aChar : str.toCharArray()) {
			if (!Character.isUpperCase(aChar)) {
				return false;
			}
		}
		return true;
	}

	public static String capitalizeFirstLetter(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length());
	}

	public static String camelToUnderScore(String str) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0 ; i < str.length() ; i++) {
			final char currChar = str.charAt(i);
			stringBuilder.append(Character.isUpperCase(currChar) ? "_" + currChar : Character.toUpperCase(currChar));
		}
		return stringBuilder.toString();
	}
}
