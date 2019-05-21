package com.ironsource.aura.aircon.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public class IntegerProperty
		extends AbstractParsedProperty<Integer> {

	public IntegerProperty(final String key, final Integer defaultValue) {
		super(key, defaultValue);
	}

	@Override
	protected Integer parse(final String property) {
		return Integer.parseInt(property);
	}

	@Override
	public Integer fromPrefs(final SharedPreferences overridePrefs) {
		return overridePrefs.getInt(getKey(), getDefaultValue());
	}
}
