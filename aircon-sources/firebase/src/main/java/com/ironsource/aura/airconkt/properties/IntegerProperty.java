package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public class IntegerProperty
        extends AbstractParsedProperty<Integer> {

    public IntegerProperty(final String key) {
        super(key);
    }

    @Override
    protected Integer parse(final String property) {
        return Integer.parseInt(property);
    }

    @Override
    public Integer fromPrefs(final SharedPreferences overridePrefs) {
        return overridePrefs.contains(getKey()) ?
                overridePrefs.getInt(getKey(), 0) : null;
    }
}
