package com.ironsource.aura.airconkt.properties;

import android.content.SharedPreferences;

/**
 * Created on 11/11/2018.
 */
public class FloatProperty
        extends AbstractParsedProperty<Float> {

    public FloatProperty(final String key) {
        super(key);
    }

    @Override
    protected Float parse(final String property) {
        return Float.parseFloat(property);
    }

    @Override
    public Float fromPrefs(final SharedPreferences overridePrefs) {
        return overridePrefs.contains(getKey()) ?
                overridePrefs.getFloat(getKey(), 0f) : null;
    }
}
