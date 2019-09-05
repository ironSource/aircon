package com.ironsource.aura.aircon.source;

import android.support.annotation.NonNull;

import com.ironsource.aura.aircon.AirConKt;
import com.ironsource.aura.aircon.common.ConfigSource;

import java.util.Set;

/**
 * Used to return default value if config source returns null
 */
public class ConfigSourceWrapper
		implements ConfigSource {

	private final ConfigSource mConfigSource;

	public ConfigSourceWrapper(@NonNull final ConfigSource configSource) {
		mConfigSource = configSource;
	}

	@Override
	public long getFirstLoadTimeMillis() {
		return mConfigSource.getFirstLoadTimeMillis();
	}

	@Override
	public Integer getInteger(final String key, final Integer defaultValue) {
		return getValue(key, mConfigSource.getInteger(key, defaultValue), defaultValue);
	}

	@Override
	public void putInteger(final String key, final Integer value) {
		mConfigSource.putInteger(key, value);
	}

	@Override
	public boolean isIntegerConfigured(final String key) {
		return mConfigSource.isIntegerConfigured(key);
	}

	@Override
	public Long getLong(final String key, final Long defaultValue) {
		return getValue(key, mConfigSource.getLong(key, defaultValue), defaultValue);
	}

	@Override
	public void putLong(final String key, final Long value) {
		mConfigSource.putLong(key, value);
	}

	@Override
	public boolean isLongConfigured(final String key) {
		return mConfigSource.isLongConfigured(key);
	}

	@Override
	public Float getFloat(final String key, final Float defaultValue) {
		return getValue(key, mConfigSource.getFloat(key, defaultValue), defaultValue);
	}

	@Override
	public void putFloat(final String key, final Float value) {
		mConfigSource.putFloat(key, value);
	}

	@Override
	public boolean isFloatConfigured(final String key) {
		return mConfigSource.isFloatConfigured(key);
	}

	@Override
	public Boolean getBoolean(final String key, final Boolean defaultValue) {
		return getValue(key, mConfigSource.getBoolean(key, defaultValue), defaultValue);
	}

	@Override
	public void putBoolean(final String key, final Boolean value) {
		mConfigSource.putBoolean(key, value);
	}

	@Override
	public boolean isBooleanConfigured(final String key) {
		return mConfigSource.isBooleanConfigured(key);
	}

	@Override
	public String getString(final String key, final String defaultValue) {
		return getValue(key, mConfigSource.getString(key, defaultValue), defaultValue);
	}

	@Override
	public void putString(final String key, final String value) {
		mConfigSource.putString(key, value);
	}

	@Override
	public boolean isStringConfigured(final String key) {
		return mConfigSource.isStringConfigured(key);
	}

	@Override
	public Set<String> getStringSet(final String key, final Set<String> defaultValue) {
		return getValue(key, mConfigSource.getStringSet(key, defaultValue), defaultValue);
	}

	@Override
	public void putStringSet(final String key, final Set<String> value) {
		mConfigSource.putStringSet(key, value);
	}

	@Override
	public boolean isStringSetConfigured(final String key) {
		return mConfigSource.isStringSetConfigured(key);
	}

	private <T> T getValue(String key, T value, T defaultValue) {
		if (value != null) {
			log("Using remote value \"%s\" -> %s", key, value);
			return value;
		}
		else {
			log("Using *default* value \"%s\" -> %s", key, defaultValue);
			return defaultValue;
		}
	}

	private void log(final String msg, Object... formatParams) {
		AirConKt.INSTANCE.getLogger()
		                 .v(toString() + " - " + String.format(msg, formatParams));
	}

	@Override
	public String toString() {
		return mConfigSource.getClass()
		                    .getSimpleName();
	}
}