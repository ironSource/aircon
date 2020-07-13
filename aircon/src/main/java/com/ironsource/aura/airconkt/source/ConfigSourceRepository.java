package com.ironsource.aura.airconkt.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ironsource.aura.airconkt.SdkContext;
import com.ironsource.aura.airconkt.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for managing config sources.
 */
public class ConfigSourceRepository {

	private final static ConfigSource STUB_CONFIG_SOURCE = new StubConfigSource();

	private final SdkContext mSdkContext;

	private final Map<Object, ConfigSource>                   mConfigSources;
	private final Map<Class, IdentifiableConfigSourceFactory> mIdentifiableConfigSourceFactories;

	public ConfigSourceRepository(SdkContext sdkContext, final List<ConfigSource> configSources, final List<IdentifiableConfigSource> identifiableConfigSources, final Map<Class, IdentifiableConfigSourceFactory> identifiableConfigSourceFactories) {
		if (sdkContext == null) {
			throw new IllegalArgumentException("Null sdk context");
		}
		mSdkContext = sdkContext;

		mConfigSources = new HashMap<>();
		for (ConfigSource configSource : configSources) {
			addSource(configSource);
		}
		for (IdentifiableConfigSource identifiableConfigSource : identifiableConfigSources) {
			addIdentifiableSource(identifiableConfigSource);
		}

		mIdentifiableConfigSourceFactories = new HashMap<>(identifiableConfigSourceFactories);
	}

	/**
	 * Add a config source.
	 * A config can have only one instance of the same class.
	 * For adding multiple config sources of the same class use {@link #addIdentifiableSource(IdentifiableConfigSource)}.
	 *
	 * @param configSource config source to add
	 */
	public synchronized void addSource(@NonNull ConfigSource configSource) {
		mConfigSources.put(configSource.getClass(), new ConfigSourceWrapper(configSource));
	}

	/**
	 * Add an identifiable config source.
	 * An identifiable config source is useful for cases where more than once instance
	 * of a ConfigSource class can be used.
	 * The config provider methods will be generated with the config id as a parameter to decide
	 * to which config source the config belongs.
	 *
	 * @param configSource an identifiable config source
	 */
	public synchronized void addIdentifiableSource(@NonNull IdentifiableConfigSource configSource) {
		mConfigSources.put(configSource.getId(), new ConfigSourceWrapper(configSource));
	}

	/**
	 * Remove a config source.
	 *
	 * @param configSource config source to remove
	 */
	public synchronized void removeSource(ConfigSource configSource) {
		mConfigSources.remove(configSource.getClass());
	}

	/**
	 * Remove an identifiable config source.
	 *
	 * @param identifiableConfigSource identifiable config source to remove
	 */
	public synchronized void removeIdentifiableSource(IdentifiableConfigSource identifiableConfigSource) {
		mConfigSources.remove(identifiableConfigSource.getId());
	}

	/**
	 * Add a factory used to construct an identifiable config source on demand.
	 * This is useful for cases where the config source is needs to be dynamically created
	 * given an ID.
	 *
	 * @param configSourceClass the factory config source class.
	 * @param factory           config source factory.
	 * @param <T> config source id type
	 * @param <S> config source type
	 * @see #addIdentifiableSource(IdentifiableConfigSource)
	 */
	public synchronized <T, S extends IdentifiableConfigSource<T>> void addIdentifiableSourceFactory(Class<S> configSourceClass, IdentifiableConfigSourceFactory<T> factory) {
		mIdentifiableConfigSourceFactories.put(configSourceClass, factory);
	}

	public synchronized ConfigSource getSource(Class<? extends ConfigSource> configSourceClass) {
		return getSource(configSourceClass, configSourceClass);
	}

	public synchronized ConfigSource getSource(Class<? extends ConfigSource> configSourceClass, Object sourceId) {
		ConfigSource configSource = mConfigSources.get(sourceId);

		if (configSource != null) {
			return configSource;
		}

		configSource = createConfigSourceFromFactory(configSourceClass, sourceId);
		if (configSource != null) {
			return configSource;
		}

		log().v("Unable to find source, returning stub source for class = " + configSourceClass.getSimpleName());

		return STUB_CONFIG_SOURCE;
	}

	private Logger log() {
		return mSdkContext.getLogger();
	}

	@Nullable
	private ConfigSource createConfigSourceFromFactory(final Class<? extends ConfigSource> configSourceClass, final Object sourceId) {
		final IdentifiableConfigSourceFactory identifiableConfigSourceFactory = mIdentifiableConfigSourceFactories.get(configSourceClass);
		if (identifiableConfigSourceFactory == null) {
			return null;
		}

		//noinspection unchecked - enforced by addIdentifiableSourceFactory() method signature.
		@NonNull final ConfigSource createdConfigSource = new ConfigSourceWrapper(identifiableConfigSourceFactory.create(sourceId));
		if (identifiableConfigSourceFactory.isSourceReusable()) {
			mConfigSources.put(sourceId, createdConfigSource);
		}

		return createdConfigSource;
	}
}
