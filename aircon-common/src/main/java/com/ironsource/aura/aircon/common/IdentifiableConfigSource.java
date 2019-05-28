package com.ironsource.aura.aircon.common;

/**
 * An interface for an identifiable config source.
 */
public interface IdentifiableConfigSource <T>
		extends ConfigSource {

	/**
	 * Return the config source identifier.
	 * @return config source id
	 */
	T getId();
}
