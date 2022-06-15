package com.ironsource.aura.aircon.source;

import com.ironsource.aura.aircon.common.ConfigSource;

import java.util.Set;

/**
 * Created on 21/4/19.
 */
public class StubConfigSource
        implements ConfigSource {

    @Override
    public long getFirstLoadTimeMillis() {
        return 0;
    }

    @Override
    public Integer getInteger(final String key, final Integer defaultValue) {
        return defaultValue;
    }

    @Override
    public void putInteger(final String key, final Integer value) {
        // No-op
    }

    @Override
    public boolean isIntegerConfigured(final String key) {
        return false;
    }

    @Override
    public Long getLong(final String key, final Long defaultValue) {
        return defaultValue;
    }

    @Override
    public void putLong(final String key, final Long value) {
        // No-op
    }

    @Override
    public boolean isLongConfigured(final String key) {
        return false;
    }

    @Override
    public Float getFloat(final String key, final Float defaultValue) {
        return defaultValue;
    }

    @Override
    public void putFloat(final String key, final Float value) {
        // No-op
    }

    @Override
    public boolean isFloatConfigured(final String key) {
        return false;
    }

    @Override
    public Boolean getBoolean(final String key, final Boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public void putBoolean(final String key, final Boolean value) {
        // No-op
    }

    @Override
    public boolean isBooleanConfigured(final String key) {
        return false;
    }

    @Override
    public String getString(final String key, final String defaultValue) {
        return defaultValue;
    }

    @Override
    public void putString(final String key, final String value) {
        // No-op
    }

    @Override
    public boolean isStringConfigured(final String key) {
        return false;
    }

    @Override
    public Set<String> getStringSet(final String key, final Set<String> defaultValue) {
        return defaultValue;
    }

    @Override
    public void putStringSet(final String key, final Set<String> value) {
        // No-op
    }

    @Override
    public boolean isStringSetConfigured(final String key) {
        return false;
    }

    @Override
    public Object getObject(String key, Object defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean isObjectConfigured(String key) {
        return false;
    }
}
