package com.ironsource.aura.aircon.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public class FloatProperty
		extends AbstractParsedProperty<Float> {

	public FloatProperty(final String key, final Float defaultValue) {
		super(key, defaultValue);
	}

	@Override
	protected Float parse(final String property) {
		return Float.parseFloat(property);
	}

	@Override
	public Float fromPrefs(final SharedPreferences overridePrefs) {
		return overridePrefs.getFloat(getKey(), getDefaultValue());
	}
}
