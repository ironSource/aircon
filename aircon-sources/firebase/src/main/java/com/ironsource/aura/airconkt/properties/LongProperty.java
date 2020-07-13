package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public class LongProperty
        extends AbstractParsedProperty<Long> {

    public LongProperty(final String key) {
        super(key);
    }

    @Override
    protected Long parse(final String property) {
        return Long.parseLong(property);
    }

    @Override
    public Long fromPrefs(final SharedPreferences overridePrefs) {
        return overridePrefs.contains(getKey()) ? overridePrefs.getLong(getKey(), 0)
                : null;
    }
}
