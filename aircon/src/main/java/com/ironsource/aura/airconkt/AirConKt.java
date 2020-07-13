package com.ironsource.aura.airconkt;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ironsource.aura.airconkt.injection.AttributeResolver;
import com.ironsource.aura.airconkt.logging.Logger;
import com.ironsource.aura.airconkt.source.ConfigSource;
import com.ironsource.aura.airconkt.source.ConfigSourceRepository;

/**
 * AirCon SDK entry point.
 * Use {@link #get()} in order to get an instance.
 */
public class AirConKt {

	private static AirConKt sInstance;

	private Context mContext;

	private Logger            mLogger;
	private Class             mAttrClass;
	private AttributeResolver mAttributeResolver;
	private JsonConverter     mJsonConverter;

	private ConfigSourceRepository mConfigSourceRepository;

	/**
	 * Returns a singleton instance of the AirCon SDK.
	 * Before any use of the SDK {@link #init(AirConConfiguration)} should be called ({@link Application#onCreate()} is the common place)
	 *
	 * @return AirCon SDK instance.
	 */
	public static synchronized AirConKt get() {
		if (sInstance == null) {
			synchronized (AirConKt.class) {
				if (sInstance == null) {
					sInstance = new AirConKt();
				}
			}
		}
		return sInstance;
	}

	/**
	 * Initializes the SDK with the provided configuration.
	 *
	 * @param airConConfiguration SDK configuration.
	 */
	public void init(@NonNull AirConConfiguration airConConfiguration) {
		if (isInitialized()) {
			return;
		}
		mContext = airConConfiguration.getContext();

		mLogger = airConConfiguration.getLogger();
		mAttrClass = airConConfiguration.getAttrClass();
		mAttributeResolver = airConConfiguration.getAttributeResolver();
		mJsonConverter = airConConfiguration.getJsonConverter();

		final SdkContext sdkContext = new SdkContext(airConConfiguration.getLogger());
		mConfigSourceRepository = new ConfigSourceRepository(sdkContext, airConConfiguration.getConfigSources(), airConConfiguration.getIdentifiableConfigSources(), airConConfiguration.getIdentifiableConfigSourceFactories());
	}

	/**
	 * Returns whether the SDK was initialized.
	 *
	 * @return true is SDK is initialized, false otherwise.
	 * @see #init(AirConConfiguration)
	 */
	public boolean isInitialized() {
		return mContext != null;
	}

	/**
	 * Returns the application context retrieved from context the SDK was initialized with.
	 *
	 * @return SDK context.
	 * @throws IllegalStateException if SDK is not initialized.
	 */
	@NonNull
	public Context getContext() {
		assertInitialized();
		return mContext;
	}

	/**
	 * Returns the SDK logger set by {@link AirConConfiguration.Builder#setLogger(Logger)}
	 * or {@link com.ironsource.aura.airconkt.logging.AndroidLogger} if non is provided.
	 *
	 * @return SDK logger.
	 * @throws IllegalStateException if SDK is not initialized.
	 */
	@NonNull
	public Logger getLogger() {
		assertInitialized();
		return mLogger;
	}

	/**
	 * Returns whether XML injection is enabled.
	 *
	 * @return true if XML injection is enabled, false otherwise.
	 * @throws IllegalStateException if SDK is not initialized.
	 * @see AirConConfiguration.Builder#enableXmlInjection(Class, AttributeResolver)
	 */
	public boolean isXmlInjectionEnabled() {
		assertInitialized();
		return mAttrClass != null;
	}

	/**
	 * Returns the attribute class if supplied, null otherwise.
	 *
	 * @return attribute class if supplied, null otherwise.
	 * @throws IllegalStateException if SDK is not initialized.
	 * @see AirConConfiguration.Builder#enableXmlInjection(Class, AttributeResolver)
	 */
	@Nullable
	public Class getAttrClass() {
		assertInitialized();
		return mAttrClass;
	}

	/**
	 * Returns attribute resolver if supplied, null otherwise.
	 *
	 * @return attribute resolver if supplied, null otherwise.
	 * @throws IllegalStateException if SDK is not initialized.
	 * @see AirConConfiguration.Builder#enableXmlInjection(Class, AttributeResolver)
	 */
	public AttributeResolver getAttributeResolver() {
		assertInitialized();
		return mAttributeResolver;
	}

	/**
	 * Returns json converter if supplied..
	 *
	 * @return json converter if supplied, null otherwise.
	 * @throws IllegalStateException if SDK is not initialized or no converter is supplied.
	 * @see AirConConfiguration.Builder#setJsonConverter(JsonConverter)
	 */
	public JsonConverter getJsonConverter() {
		assertInitialized();

		if (mJsonConverter == null) {
			throw new IllegalStateException("No json converter available, a converter needs to be supplied in the AirConConfiguration");
		}

		return mJsonConverter;
	}

	/**
	 * Returns config sources repository through which {@link ConfigSource}
	 * can be added, removed and retrieved.
	 *
	 * @return config source repository.
	 * @throws IllegalStateException if SDK is not initialized.
	 */
	@NonNull
	public ConfigSourceRepository getConfigSourceRepository() {
		assertInitialized();
		return mConfigSourceRepository;
	}

	private void assertInitialized() {
		if (!isInitialized()) {
			throw new IllegalStateException("Sdk not initialized, did you call AirCon.init()?");
		}
	}
}