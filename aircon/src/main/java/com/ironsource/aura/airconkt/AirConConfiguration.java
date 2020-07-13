package com.ironsource.aura.airconkt;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;

import com.ironsource.aura.airconkt.injection.AttributeResolver;
import com.ironsource.aura.airconkt.logging.AndroidLogger;
import com.ironsource.aura.airconkt.logging.Logger;
import com.ironsource.aura.airconkt.logging.LoggerWrapper;
import com.ironsource.aura.airconkt.source.ConfigSource;
import com.ironsource.aura.airconkt.source.IdentifiableConfigSource;
import com.ironsource.aura.airconkt.source.IdentifiableConfigSourceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class used to initialize the SDK.
 *
 * @see AirConKt#init(AirConConfiguration)
 */
public class AirConConfiguration {

    private final Context mContext;
    private final Logger mLogger;
    private final Class mAttrClass;
    private final AttributeResolver mAttributeResolver;
    private final JsonConverter mJsonConverter;
    private final List<ConfigSource> mConfigSources;
    private final List<IdentifiableConfigSource> mIdentifiableConfigSources;
    private final Map<Class, IdentifiableConfigSourceFactory> mIdentifiableConfigSourceFactories;

    private AirConConfiguration(final Context context, final Logger logger, final Class attrClass, final AttributeResolver attributeResolver, final JsonConverter jsonConverter, final List<ConfigSource> configSources, final List<IdentifiableConfigSource> identifiableConfigSources, final Map<Class, IdentifiableConfigSourceFactory> identifiableConfigSourceFactories) {
        mContext = context;
        mLogger = logger;
        mAttrClass = attrClass;
        mAttributeResolver = attributeResolver;
        mJsonConverter = jsonConverter;
        mConfigSources = configSources;
        mIdentifiableConfigSources = identifiableConfigSources;
        mIdentifiableConfigSourceFactories = identifiableConfigSourceFactories;
    }

    Context getContext() {
        return mContext;
    }

    Logger getLogger() {
        return mLogger;
    }

    Class getAttrClass() {
        return mAttrClass;
    }

    AttributeResolver getAttributeResolver() {
        return mAttributeResolver;
    }

    public JsonConverter getJsonConverter() {
        return mJsonConverter;
    }

    List<ConfigSource> getConfigSources() {
        return mConfigSources;
    }

    List<IdentifiableConfigSource> getIdentifiableConfigSources() {
        return mIdentifiableConfigSources;
    }

    Map<Class, IdentifiableConfigSourceFactory> getIdentifiableConfigSourceFactories() {
        return mIdentifiableConfigSourceFactories;
    }

    public static class Builder {

        private final Context mContext;

        private Logger mLogger;
        private boolean mLoggingEnabled;
        private Class mAttrClass;
        private AttributeResolver mAttributeResolver;
        private JsonConverter mJsonConverter;

        private final List<ConfigSource> mConfigSources;
        private final List<IdentifiableConfigSource> mIdentifiableConfigSources;
        private final Map<Class, IdentifiableConfigSourceFactory> mIdentifiableConfigSourceFactories;

        /**
         * Constructs a Builder with the application context retrieved from the supplied context.
         *
         * @param context context to create config with.
         */
        public Builder(@NonNull final Context context) {
            mContext = context.getApplicationContext();
            mLogger = new AndroidLogger();
            mLoggingEnabled = true;
            mConfigSources = new ArrayList<>();
            mIdentifiableConfigSources = new ArrayList<>();
            mIdentifiableConfigSourceFactories = new HashMap<>();
        }

        /**
         * Set a logger to be used by the SDK.
         * If a logger is not supplied {@link com.ironsource.aura.airconkt.logging.AndroidLogger} is used.
         *
         * @param logger logger
         * @return this {@link Builder} instance.
         * @see #setLoggingEnabled(boolean)
         */
        public Builder setLogger(@NonNull final Logger logger) {
            mLogger = logger;
            return this;
        }

        /**
         * Set whether SDK logging is enabled (true by default).
         *
         * @param loggingEnabled whether logging should be enabled
         * @return this {@link Builder} instance.
         * @see #setLogger(Logger)
         */
        public Builder setLoggingEnabled(final boolean loggingEnabled) {
            mLoggingEnabled = loggingEnabled;
            return this;
        }

        /**
         * Set whether XML attribute injection is enabled.
         * The app attribute class is used to resolve attributes from the XML.
         *
         * @param attrClass    the app R.attr.class object.
         * @param configSource config source for remote values
         * @return this {@link Builder} instance.
         * @see com.ironsource.aura.airconkt.injection.AirConContextWrapper
         * @see com.ironsource.aura.airconkt.injection.AirConAppCompatActivity
         * @see com.ironsource.aura.airconkt.injection.AirConFragmentActivity
         */
        public Builder enableXmlInjection(@NonNull Class attrClass, ConfigSource configSource) {
            return enableXmlInjection(attrClass, new ConfigSourceAttributeResolver(configSource));
        }

        /**
         * Set whether XML attribute injection is enabled.
         * The app attribute class is used to resolve attributes from the XML.
         *
         * @param attrClass         the app R.attr.class object.
         * @param attributeResolver resolver for attributes
         * @return this {@link Builder} instance.
         * @see com.ironsource.aura.airconkt.injection.AirConContextWrapper
         * @see com.ironsource.aura.airconkt.injection.AirConAppCompatActivity
         * @see com.ironsource.aura.airconkt.injection.AirConFragmentActivity
         */
        public Builder enableXmlInjection(@NonNull Class attrClass, AttributeResolver attributeResolver) {
            mAttrClass = attrClass;
            mAttributeResolver = attributeResolver;
            return this;
        }

        /**
         * Set a json converter to be used with @JsonConfig.
         *
         * @param jsonConverter json converter
         * @return this {@link Builder} instance.
         */
        public Builder setJsonConverter(final JsonConverter jsonConverter) {
            mJsonConverter = jsonConverter;
            return this;
        }

        /**
         * Add a config source.
         * A config can have only one instance of the same class.
         * For adding multiple config sources of the same class use {@link #addIdentifiableSource(IdentifiableConfigSource)}.
         *
         * @param configSource config source to add
         * @return this {@link Builder} instance.
         */
        public Builder addConfigSource(@NonNull ConfigSource configSource) {
            mConfigSources.add(configSource);
            return this;
        }

        /**
         * Add an identifiable config source.
         * An identifiable config source is useful for cases where more than once instance
         * of a ConfigSource class can be used.
         * The config provider methods will be generated with the config id as a parameter to decide
         * to which config source the config belongs.
         *
         * @param configSource an identifiable config source
         * @return this {@link Builder} instance.
         */
        public Builder addIdentifiableSource(@NonNull IdentifiableConfigSource configSource) {
            mIdentifiableConfigSources.add(configSource);
            return this;
        }

        /**
         * Add a factory used to construct an identifiable config source on demand.
         * This is useful for cases where the config source is needs to be dynamically created
         * given an ID.
         *
         * @param configSourceClass the factory config source class.
         * @param factory           config source factory.
         * @param <T>               config source id type
         * @param <S>               config source type
         * @return this {@link Builder} instance.
         * @see #addIdentifiableSource(IdentifiableConfigSource)
         */
        public <T, S extends IdentifiableConfigSource<T>> Builder addIdentifiableSourceFactory(Class<S> configSourceClass, IdentifiableConfigSourceFactory<T> factory) {
            mIdentifiableConfigSourceFactories.put(configSourceClass, factory);
            return this;
        }

        /**
         * Constructs an {@link AirConConfiguration} object using this Builder fields.
         *
         * @return an {@link AirConConfiguration} object.
         */
        public AirConConfiguration build() {
            return new AirConConfiguration(mContext, new LoggerWrapper(mLogger, mLoggingEnabled), mAttrClass, mAttributeResolver, mJsonConverter, mConfigSources, mIdentifiableConfigSources, mIdentifiableConfigSourceFactories);
        }
    }

    private static class ConfigSourceAttributeResolver
            implements AttributeResolver {

        private final ConfigSource mConfigSource;

        private ConfigSourceAttributeResolver(final ConfigSource configSource) {
            mConfigSource = configSource;
        }

        @Override
        public ColorStateList getColorStateList(final String attrName) {
            return null;
        }

        @Override
        public Integer getColor(final String attrName) {
            return mConfigSource.getInteger(attrName, null);
        }

        @Override
        public String getString(final String attrName) {
            return mConfigSource.getString(attrName, null);
        }
    }
}