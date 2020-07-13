package com.ironsource.aura.airconkt.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.ironsource.aura.airconkt.AirConKt;
import com.ironsource.aura.airconkt.properties.AbstractProperty;
import com.ironsource.aura.airconkt.properties.BooleanProperty;
import com.ironsource.aura.airconkt.properties.FloatProperty;
import com.ironsource.aura.airconkt.properties.IntegerProperty;
import com.ironsource.aura.airconkt.properties.LongProperty;
import com.ironsource.aura.airconkt.properties.StringProperty;

/**
 * Created on 4/25/2019.
 */
public class FireBaseConfigSource
        implements ConfigSource {

    private static final String PREF_OVERRIDES = "overrides";

    private final FirebaseRemoteConfig mRemoteConfig;
    private final SharedPreferences mOverridePrefs;

    public FireBaseConfigSource(Context context, FirebaseRemoteConfig remoteConfig) {
        mRemoteConfig = remoteConfig;
        mOverridePrefs = context.getSharedPreferences(PREF_OVERRIDES, Context.MODE_PRIVATE);
    }

    @Override
    public Integer getInteger(final String key) {
        return get(new IntegerProperty(key));
    }

    @Override
    public void putInteger(final String key, final Integer value) {
        final SharedPreferences.Editor edit = mOverridePrefs.edit();
        if (value != null) {
            edit.putInt(key, value);
        } else {
            edit.remove(key);
        }
        edit.apply();
    }

    @Override
    public Long getLong(final String key) {
        return get(new LongProperty(key));
    }

    @Override
    public void putLong(final String key, final Long value) {
        final SharedPreferences.Editor edit = mOverridePrefs.edit();
        if (value != null) {
            edit.putLong(key, value);
        } else {
            edit.remove(key);
        }
        edit.apply();
    }

    @Override
    public Float getFloat(final String key) {
        return get(new FloatProperty(key));
    }

    @Override
    public void putFloat(final String key, final Float value) {
        final SharedPreferences.Editor edit = mOverridePrefs.edit();
        if (value != null) {
            edit.putFloat(key, value);
        } else {
            edit.remove(key);
        }
        edit.apply();
    }

    @Override
    public Boolean getBoolean(final String key) {
        return get(new BooleanProperty(key));
    }

    @Override
    public void putBoolean(final String key, final Boolean value) {
        final SharedPreferences.Editor edit = mOverridePrefs.edit();
        if (value != null) {
            edit.putBoolean(key, value);
        } else {
            edit.remove(key);
        }
        edit.apply();
    }

    @Override
    public String getString(final String key) {
        return get(new StringProperty(key));
    }

    @Override
    public void putString(final String key, final String value) {
        mOverridePrefs.edit()
                .putString(key, value)
                .apply();
    }

    @Override
    public Object getObject(String key) {
        return null;
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
        AirConKt.get()
                .getLogger()
                .v(toString() + " - " + String.format(msg, formatParams));
    }
}
