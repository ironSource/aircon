package com.ironsource.aura.aircon.source;

import androidx.annotation.NonNull;

/**
 * A factory for {@link IdentifiableConfigSource}.
 * Used to create sources on demand.
 */
public interface IdentifiableConfigSourceFactory <T> {

	/**
	 * Create an {@link IdentifiableConfigSource} with the provided id.
	 * @param id id to create a source for
	 * @return {@link IdentifiableConfigSource} for the supplied id.
	 */
	@NonNull
	IdentifiableConfigSource<T> create(T id);

	/**
	 * Whether to call {@link #create(Object)} once and cache and reuse the {@link IdentifiableConfigSource} created
	 * or call {@link #create(Object)} every time a source is needed.
	 *
	 * @return true if the {@link IdentifiableConfigSource} created by calling {@link #create(Object)} should be reused.
	 */
	boolean isSourceReusable();
}
