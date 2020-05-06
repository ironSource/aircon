package com.ironsource.aura.aircon.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.ironsource.aura.aircon.AirCon;
import com.ironsource.aura.aircon.JsonConverter;
import com.ironsource.aura.aircon.logging.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created on 11/12/2018.
 */
public class AirConUtils {

    private static Map<String, Enum> RANDOMS_VALUES = new HashMap<>();

    public static boolean isValidUrl(final String url) {
        final boolean validUrl = URLUtil.isValidUrl(url);
        if (!validUrl) {
            log().e("Got invalid url: " + url);
        }
        return validUrl;
    }

    public static ColorInt hexToColorInt(String colorInHex) {
        if (!TextUtils.isEmpty(colorInHex)) {
            try {
                return new ColorInt(Color.parseColor(colorInHex));
            } catch (Exception e) {
                log().e("Failed to parse color hex: " + colorInHex);
                log().logException(e);
            }
        }

        return null;
    }

    public static String getColorResAsHex(Resources resources, int colorRes) {
        return "#" + Integer.toHexString(resources.getColor(colorRes));
    }

    public static String colorIntToHex(ColorInt color) {
        return "#" + Integer.toHexString(color.get());
    }

    public static String toJson(Object object, @NonNull JsonConverter converter) {
        return converter.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz, String defaultValue, @NonNull JsonConverter converter) {
        try {
            return converter.fromJson(json, clazz);
        } catch (JsonConverter.JsonException e) {
            log().e("Failed to parse json: " + json);
            log().logException(e);
            return defaultValue != null && !TextUtils.equals(json, defaultValue) ? fromJson(defaultValue, clazz, defaultValue, converter) : null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Type type, String defaultValue, @NonNull JsonConverter converter) {
        try {
            return converter.fromJson(json, type);
        } catch (JsonConverter.JsonException e) {
            log().e("Failed to parse json: " + json);
            log().logException(e);
            return defaultValue != null && !TextUtils.equals(json, defaultValue) ? (T) fromJson(defaultValue, type, defaultValue, converter) : null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getRandomEnumValue(String configKey, Class<T> enumClass) {
        final Enum cachedRandom = RANDOMS_VALUES.get(configKey);
        if (cachedRandom != null) {
            return (T) cachedRandom;
        }

        final T[] enumConstants = enumClass.getEnumConstants();

        final int randomIndex = new Random().nextInt(enumConstants.length);
        final T randomEnumValue = enumConstants[randomIndex];

        RANDOMS_VALUES.put(configKey, randomEnumValue);

        return randomEnumValue;
    }

    public static <T extends Annotation> T getCustomConfigAnnotation(Class featureClass, Class<T> annotationClass, String fieldName) {
        try {
            return featureClass.getDeclaredField(fieldName)
                    .getAnnotation(annotationClass);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static Logger log() {
        return AirCon.get()
                .getLogger();
    }
}
