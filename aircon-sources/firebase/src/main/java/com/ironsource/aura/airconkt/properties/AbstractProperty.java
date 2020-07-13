package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public abstract class AbstractProperty <T> {

	private String mKey;

	public AbstractProperty(final String key) {
		mKey = key;
	}

	public abstract T fromPrefs(final SharedPreferences overridePrefs);

	public String getKey() {
		return mKey;
	}

	public T asType(String property) {
		return property != null ? convertToType(property) : null;
	}

	protected abstract T convertToType(String property);
}
