package com.ironsource.aura.aircon.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public abstract class AbstractProperty <T> {

	private String mKey;
	private T      mDefaultValue;

	public AbstractProperty(final String key, final T defaultValue) {
		mKey = key;
		mDefaultValue = defaultValue;
	}

	public abstract T fromPrefs(final SharedPreferences overridePrefs);

	public String getKey() {
		return mKey;
	}

	public T getDefaultValue() {
		return mDefaultValue;
	}

	public T asType(String property) {
		return property != null ? convertToType(property) : null;
	}

	protected abstract T convertToType(String property);
}
