package com.ironsource.aura.airconkt.source

/**
 * Interface for config source.
 */
interface ConfigSource {

    /**
     * Return an Integer using the provided key.
     * If no mapping exists for the key or the key value is not an Integer, return null.
     *
     * @param key          config key.
     * @return the configured Integer for the key, null otherwise.
     */
    fun getInteger(key: String): Int?

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to [.getInteger] will return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    fun putInteger(key: String,
                   value: Int?)

    /**
     * Return an Long using the provided key.
     * If no mapping exists for the key or the key value is not an Long, return null.
     *
     * @param key          config key.
     * @return the configured Long for the key, null otherwise.
     */
    fun getLong(key: String): Long?

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to [.getLong] will return this value.
     *
     * @param key   config key.
     */
    fun putLong(key: String,
                value: Long?)

    /**
     * Return a Float using the provided key.
     * If no mapping exists for the key or the key value is not a Float, return null.
     *
     * @param key          config key.
     * @return the configured Float for the key, null otherwise.
     */
    fun getFloat(key: String): Float?

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to [.getFloat] will return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    fun putFloat(key: String,
                 value: Float?)

    /**
     * Return a Boolean using the provided key.
     * If no mapping exists for the key or the key value is not a Boolean, return null.
     *
     * @param key          config key.
     * @return the configured Boolean for the key, null otherwise.
     */
    fun getBoolean(key: String): Boolean?

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to [.getBoolean] will return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    fun putBoolean(key: String,
                   value: Boolean?)

    /**
     * Return a String using the provided key.
     * If no mapping exists for the key or the key value is not a String, return null.
     *
     * @param key          config key.
     * @return the configured String for the key, null otherwise.
     */
    fun getString(key: String): String?

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to [.getString] will return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    fun putString(key: String,
                  value: String?)

    /**
     * Return any value using the provided key.
     * If no mapping exists for the key, return null.
     *
     * @param key          config key.
     * @return the configured value for the key, null otherwise.
     */
    fun getAny(key: String): Any?

    /**
     * Override the mapping to the provided key with the provided value.
     * Any subsequent calls to [.getAny] will return this value.
     *
     * @param key   config key.
     * @param value override value.
     */
    fun putAny(key: String,
               value: Any?)
}