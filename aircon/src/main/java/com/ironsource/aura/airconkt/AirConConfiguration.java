package com.ironsource.aura.airconkt;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;

import com.ironsource.aura.airconkt.injection.AttributeResolver;
import com.ironsource.aura.airconkt.logging.AndroidLogger;
import com.ironsource.aura.airconkt.logging.ControllableLogger;
import com.ironsource.aura.airconkt.logging.Logger;
import com.ironsource.aura.airconkt.source.ConfigSource;

import java.util.ArrayList;
import java.util.List;

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

    private AirConConfiguration(final Context context, final Logger logger, final Class attrClass, final AttributeResolver attributeResolver, final JsonConverter jsonConverter, final List<ConfigSource> configSources) {
        mContext = context;
        mLogger = logger;
        mAttrClass = attrClass;
        mAttributeResolver = attributeResolver;
        mJsonConverter = jsonConverter;
        mConfigSources = configSources;
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

    public static class Builder {

        private final Context mContext;

        private Logger mLogger;
        private boolean mLoggingEnabled;
        private Class mAttrClass;
        private AttributeResolver mAttributeResolver;
        private JsonConverter mJsonConverter;

        private final List<ConfigSource> mConfigSources;

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
         * Set a json converter to be used with @Json
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
         *
         * @param configSource config source to add
         * @return this {@link Builder} instance.
         */
        public Builder addConfigSource(@NonNull ConfigSource configSource) {
            mConfigSources.add(configSource);
            return this;
        }

        /**
         * Constructs an {@link AirConConfiguration} object using this Builder fields.
         *
         * @return an {@link AirConConfiguration} object.
         */
        public AirConConfiguration build() {
            return new AirConConfiguration(mContext, new ControllableLogger(mLogger, mLoggingEnabled), mAttrClass, mAttributeResolver, mJsonConverter, mConfigSources);
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
            return mConfigSource.getInteger(attrName);
        }

        @Override
        public String getString(final String attrName) {
            return mConfigSource.getString(attrName);
        }
    }
}