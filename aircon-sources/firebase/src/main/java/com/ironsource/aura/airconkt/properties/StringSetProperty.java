package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 11/11/2018.
 */
public class StringSetProperty
		extends AbstractParsedProperty<Set<String>> {

	private static final String DELIMITER = ",";

	public StringSetProperty(final String key, final Set<String> defaultValue) {
		super(key, defaultValue);
	}

	@Override
	protected Set<String> parse(final String property) {
		if (property == null) {
			return null;
		}
		final String[] arr = property.split(DELIMITER);
		for (int i = 0 ; i < arr.length ; i++) {
			arr[i] = arr[i].trim();
		}
		return new HashSet<>(Arrays.asList(arr));
	}

	@Override
	public Set<String> fromPrefs(final SharedPreferences overridePrefs) {
		return overridePrefs.getStringSet(getKey(), null);
	}
}