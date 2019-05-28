package com.ironsource.aura.aircon.compiler.model.element;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * Created on 11/14/2018.
 */
public class ConfigGroupElement
		extends AbstractConfigElement<ConfigGroupElement.Properties> {

	private final List<AbstractConfigElement> mConfigElements;
	private final boolean                     mFeatureGroup;

	ConfigGroupElement(final Properties properties, final List<AbstractConfigElement> configElements, final boolean featureGroup) {
		super(properties);
		mConfigElements = configElements;
		mFeatureGroup = featureGroup;
	}

	public List<AbstractConfigElement> getConfigElements() {
		return mConfigElements;
	}

	public boolean isFeatureGroup() {
		return mFeatureGroup;
	}

	static class Properties
			extends AbstractConfigElement.Properties {

		public Properties(final String name, final String key, final TypeName type, final ClassName providerClass) {
			super(name, key, type, providerClass);
		}
	}
}
