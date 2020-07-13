package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public class BooleanProperty
        extends AbstractProperty<Boolean> {

    public BooleanProperty(final String key) {
        super(key);
    }

    @Override
    public Boolean convertToType(final String property) {
        final boolean correctType = Boolean.TRUE.toString()
                .equalsIgnoreCase(property) || Boolean.FALSE.toString()
                .equalsIgnoreCase(property);
        return correctType ? Boolean.parseBoolean(property) : null;
    }

    @Override
    public Boolean fromPrefs(final SharedPreferences overridePrefs) {
        return overridePrefs.contains(getKey()) ?
                overridePrefs.getBoolean(getKey(), false) : null;
    }
}
