package com.ironsource.aura.airconkt.source;

import java.util.Set;

/**
 * Interface for config source.
 */
public interface ConfigSource {

    /**
     * Get the first time a config was ever loaded.
     *
     * @return first config load time in millis
     */
    long getFirstLoadTimeMillis();

    /**
     * Return an Integer using the provided key.
     * If no mapping exists for the key or the key value is not an Integer, return null.
     *
     * @param key          config key.
     * @param defaultValue the config default value.
     * @return the configured Integer for the key, null otherwise.
     */
    Integer getInteger(String key, final Integer defaultValue);

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to {@link #getInteger(String, Integer)} should return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    void putInteger(String key, Integer value);

    /**
     * Returns whether the provided key is mapped to an Integer value.
     *
     * @param key config key.
     * @return true if the an Integer value exists for the key, false otherwise
     */
    boolean isIntegerConfigured(final String key);

    /**
     * Return an Long using the provided key.
     * If no mapping exists for the key or the key value is not an Long, return null.
     *
     * @param key          config key.
     * @param defaultValue the config default value.
     * @return the configured Long for the key, null otherwise.
     */
    Long getLong(String key, final Long defaultValue);

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to {@link #getLong(String, Long)} should return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    void putLong(String key, Long value);

    /**
     * Returns whether the provided key is mapped to an Long value.
     *
     * @param key config key.
     * @return true if the an Long value exists for the key, false otherwise
     */
    boolean isLongConfigured(final String key);

    /**
     * Return a Float using the provided key.
     * If no mapping exists for the key or the key value is not a Float, return null.
     *
     * @param key          config key.
     * @param defaultValue the config default value.
     * @return the configured Float for the key, null otherwise.
     */
    Float getFloat(String key, final Float defaultValue);

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to {@link #getFloat(String, Float)} should return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    void putFloat(String key, Float value);

    /**
     * Returns whether the provided key is mapped to an Float value.
     *
     * @param key config key.
     * @return true if the an Float value exists for the key, false otherwise
     */
    boolean isFloatConfigured(final String key);

    /**
     * Return a Boolean using the provided key.
     * If no mapping exists for the key or the key value is not a Boolean, return null.
     *
     * @param key          config key.
     * @param defaultValue the config default value.
     * @return the configured Boolean for the key, null otherwise.
     */
    Boolean getBoolean(String key, final Boolean defaultValue);

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to {@link #getBoolean(String, Boolean)} should return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    void putBoolean(String key, Boolean value);

    /**
     * Returns whether the provided key is mapped to an Boolean value.
     *
     * @param key config key.
     * @return true if the an Boolean value exists for the key, false otherwise
     */
    boolean isBooleanConfigured(final String key);

    /**
     * Return a String using the provided key.
     * If no mapping exists for the key or the key value is not a String, return null.
     *
     * @param key          config key.
     * @param defaultValue the config default value.
     * @return the configured String for the key, null otherwise.
     */
    String getString(String key, final String defaultValue);

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to {@link #getString(String, String)} should return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    void putString(String key, String value);

    /**
     * Returns whether the provided key is mapped to an String value.
     *
     * @param key config key.
     * @return true if the an String value exists for the key, false otherwise
     */
    boolean isStringConfigured(final String key);

    /**
     * Return a String set using the provided key.
     * If no mapping exists for the key or the key value is not a String set, return null.
     *
     * @param key          config key.
     * @param defaultValue the config default value.
     * @return the configured String set for the key, null otherwise.
     */
    Set<String> getStringSet(String key, final Set<String> defaultValue);

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to {@link #getStringSet(String, Set)} should return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    void putStringSet(String key, Set<String> value);

    /**
     * Returns whether the provided key is mapped to an String set value.
     *
     * @param key config key.
     * @return true if the an String set value exists for the key, false otherwise
     */
    boolean isStringSetConfigured(final String key);

    /**
     * Experimental feature, implement empty
     */
    Object getObject(String key, Object defaultValue);

    /**
     * Experimental feature, implement empty
     */
    boolean isObjectConfigured(String key);
}
