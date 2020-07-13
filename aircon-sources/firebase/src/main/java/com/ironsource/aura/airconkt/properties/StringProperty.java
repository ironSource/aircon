package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created on 11/11/2018.
 */
public class StringProperty
		extends AbstractProperty<String> {

	public StringProperty(final String key, final String defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String convertToType(final String property) {
		return !TextUtils.isEmpty(property) ? property : null;
	}

	@Override
	public String fromPrefs(final SharedPreferences overridePrefs) {
		return overridePrefs.getString(getKey(), getDefaultValue());
	}
}
