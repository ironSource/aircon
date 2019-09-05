package com.ironsource.aura.aircon.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.ironsource.aura.aircon.AirCon;
import com.ironsource.aura.aircon.common.ConfigSource;
import com.ironsource.aura.aircon.properties.AbstractProperty;
import com.ironsource.aura.aircon.properties.BooleanProperty;
import com.ironsource.aura.aircon.properties.FloatProperty;
import com.ironsource.aura.aircon.properties.IntegerProperty;
import com.ironsource.aura.aircon.properties.LongProperty;
import com.ironsource.aura.aircon.properties.StringProperty;
import com.ironsource.aura.aircon.properties.StringSetProperty;

import java.util.Set;

/**
 * Created on 4/25/2019.
 */
public class FireBaseConfigSource
		implements ConfigSource {

	private static final String PREF_OVERRIDES = "overrides";

	private final FirebaseRemoteConfig mRemoteConfig;
	private final SharedPreferences    mOverridePrefs;

	private int mFirstLoadTimeMillis;

	public FireBaseConfigSource(Context context, FirebaseRemoteConfig remoteConfig) {
		mRemoteConfig = remoteConfig;
		mOverridePrefs = context.getSharedPreferences(PREF_OVERRIDES, Context.MODE_PRIVATE);
	}

	public void setFirstLoadTimeMillis(final int firstLoadTimeMillis) {
		mFirstLoadTimeMillis = firstLoadTimeMillis;
	}

	@Override
	public long getFirstLoadTimeMillis() {
		return mFirstLoadTimeMillis;
	}

	@Override
	public Integer getInteger(final String key, final Integer defaultValue) {
		return get(new IntegerProperty(key, defaultValue));
	}

	@Override
	public void putInteger(final String key, final Integer value) {
		final SharedPreferences.Editor edit = mOverridePrefs.edit();
		if (value != null) {
			edit.putInt(key, value);
		}
		else {
			edit.remove(key);
		}
		edit.apply();
	}

	@Override
	public boolean isIntegerConfigured(final String key) {
		return getInteger(key, 0) != null;
	}

	@Override
	public Long getLong(final String key, final Long defaultValue) {
		return get(new LongProperty(key, defaultValue));
	}

	@Override
	public void putLong(final String key, final Long value) {
		final SharedPreferences.Editor edit = mOverridePrefs.edit();
		if (value != null) {
			edit.putLong(key, value);
		}
		else {
			edit.remove(key);
		}
		edit.apply();
	}

	@Override
	public boolean isLongConfigured(final String key) {
		return getLong(key, null) != null;
	}

	@Override
	public Float getFloat(final String key, final Float defaultValue) {
		return get(new FloatProperty(key, defaultValue));
	}

	@Override
	public void putFloat(final String key, final Float value) {
		final SharedPreferences.Editor edit = mOverridePrefs.edit();
		if (value != null) {
			edit.putFloat(key, value);
		}
		else {
			edit.remove(key);
		}
		edit.apply();
	}

	@Override
	public boolean isFloatConfigured(final String key) {
		return getFloat(key, null) != null;
	}

	@Override
	public Boolean getBoolean(final String key, final Boolean defaultValue) {
		return get(new BooleanProperty(key, defaultValue));
	}

	@Override
	public void putBoolean(final String key, final Boolean value) {
		final SharedPreferences.Editor edit = mOverridePrefs.edit();
		if (value != null) {
			edit.putBoolean(key, value);
		}
		else {
			edit.remove(key);
		}
		edit.apply();
	}

	@Override
	public boolean isBooleanConfigured(final String key) {
		return getBoolean(key, null) != null;
	}

	@Override
	public String getString(final String key, final String defaultValue) {
		return get(new StringProperty(key, defaultValue));
	}

	@Override
	public void putString(final String key, final String value) {
		mOverridePrefs.edit()
		              .putString(key, value)
		              .apply();
	}

	@Override
	public boolean isStringConfigured(final String key) {
		return getString(key, null) != null;
	}

	@Override
	public Set<String> getStringSet(final String key, final Set<String> defaultValue) {
		return get(new StringSetProperty(key, defaultValue));
	}

	@Override
	public void putStringSet(final String key, final Set<String> value) {
		final SharedPreferences.Editor edit = mOverridePrefs.edit();
		if (value != null) {
			edit.putStringSet(key, value);
		}
		else {
			edit.remove(key);
		}
		edit.apply();
	}

	@Override
	public boolean isStringSetConfigured(final String key) {
		return getStringSet(key, null) != null;
	}

	private <T> T get(AbstractProperty<T> property) {
		if (mOverridePrefs.contains(property.getKey())) {
			final T overrideValue = property.fromPrefs(mOverridePrefs);
			if (overrideValue != null) {
				log("Using local override value \"%s\" -> %s", property.getKey(), overrideValue);
				return overrideValue;
			}
		}

		final String strValue = mRemoteConfig.getString(property.getKey());
		return property.asType(strValue);
	}

	private void log(final String msg, Object... formatParams) {
		AirCon.INSTANCE.getLogger()
		               .v(toString() + " - " + String.format(msg, formatParams));
	}
}
