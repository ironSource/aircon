package com.ironsource.aura.aircon

import android.content.Context
import com.ironsource.aura.aircon.AirCon.init
import com.ironsource.aura.aircon.common.ConfigTypeResolver
import com.ironsource.aura.aircon.injection.AttributeResolver
import com.ironsource.aura.aircon.logging.Logger
import com.ironsource.aura.aircon.source.ConfigSourceRepository
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * AirCon SDK entry point.
 * Before any use of the SDK [AirCon][init] should be called (Application.onCreate() is the common place)
 */
@Suppress("MemberVisibilityCanBePrivate")
object AirCon {

    /**
     * Whether the SDK was initialized.
     *
     * @see init
     */
    var initialized: Boolean = false
        private set

    /**
     * The application context retrieved from context the SDK was initialized with.
     *
     * @throws IllegalStateException if SDK is not initialized.
     */
    var context: Context? by InitializedBarrierDelegate()
        private set

    /**
     * The SDK logger set by [AirConConfiguration.Builder.setLogger]
     * or [com.ironsource.aura.aircon.logging.AndroidLogger] if non is provided.
     *
     * @throws IllegalStateException if SDK is not initialized.
     */
    var logger: Logger? by InitializedBarrierDelegate()
        private set

    /**
     * Attribute class if supplied
     *
     * @throws IllegalStateException if SDK is not initialized.
     * @see AirConConfiguration.Builder.enableXmlInjection
     */
    var attrClass: Class<*>? by InitializedBarrierDelegate()
        private set

    /**
     * Attribute resolver if supplied
     *
     * @throws IllegalStateException if SDK is not initialized.
     * @see AirConConfiguration.Builder.enableXmlInjection
     */
    var attributeResolver: AttributeResolver? by InitializedBarrierDelegate()
        private set

    /**
     * Returns json converter if supplied.
     *
     * @throws IllegalStateException if SDK is not initialized or no converter is supplied.
     * @see AirConConfiguration.Builder.setJsonConverter
     */
    var jsonConverter: JsonConverter? by InitializedBarrierDelegate {
        check(it != null) { "No json converter available, a converter needs to be supplied in the AirConConfiguration" }
    }
        private set

    /**
     * Config sources repository through which [com.ironsource.aura.aircon.common.ConfigSource]
     * can be added, removed and retrieved.
     *
     * @throws IllegalStateException if SDK is not initialized.
     */
    var configSourceRepository: ConfigSourceRepository? by InitializedBarrierDelegate()
        private set

    private lateinit var mConfigTypes: Map<Class<out Annotation>, ConfigTypeResolver<*, *, *>>

    /**
     * Initializes the SDK with the provided configuration.
     *
     * @param airConConfiguration SDK configuration.
     */
    fun init(airConConfiguration: AirConConfiguration) {
        if (initialized) {
            return
        }

        initialized = true

        context = airConConfiguration.context

        logger = airConConfiguration.logger
        attrClass = airConConfiguration.attrClass
        attributeResolver = airConConfiguration.attributeResolver
        jsonConverter = airConConfiguration.jsonConverter
        mConfigTypes = airConConfiguration.configTypes

        val sdkContext = SdkContext(airConConfiguration.logger)
        configSourceRepository = ConfigSourceRepository(sdkContext, airConConfiguration.configSources, airConConfiguration.identifiableConfigSources, airConConfiguration.identifiableConfigSourceFactories)
    }

    /**
     * Returns whether XML injection is enabled.
     *
     * @throws IllegalStateException if SDK is not initialized.
     * @see AirConConfiguration.Builder.enableXmlInjection
     */
    fun isXmlInjectionEnabled(): Boolean {
        assertInitialized()

        return attrClass != null
    }

    @Suppress("UNCHECKED_CAST")
    fun <A : Annotation, T, S> getConfigTypeResolver(configTypeAnnotation: Class<A>): ConfigTypeResolver<A, T, S> {
        assertInitialized()

        val configTypeResolver = mConfigTypes[configTypeAnnotation] as ConfigTypeResolver<A, T, S>?

        checkNotNull(configTypeResolver) {
            "No config resolver found for ${configTypeAnnotation.simpleName}, custom config types should be registered in the AirConConfiguration"
        }

        return configTypeResolver
    }
}

private class InitializedBarrierDelegate<T>(private val extCheck: ((T?) -> Unit)? = null) : ReadWriteProperty<AirCon, T?> {
    var value: T? = null

    override fun getValue(thisRef: AirCon, property: KProperty<*>): T? {
        assertInitialized()

        extCheck?.invoke(value)

        return value
    }

    override fun setValue(thisRef: AirCon, property: KProperty<*>, value: T?) {
        this.value = value
    }
}

private fun assertInitialized() {
    check(AirCon.initialized) { "Sdk not initialized, did you call AirCon.init()?" }
}
