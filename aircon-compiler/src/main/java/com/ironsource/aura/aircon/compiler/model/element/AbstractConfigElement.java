package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.compiler.model.generated.GeneratedMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * Created on 11/14/2018.
 */
public abstract class AbstractConfigElement <T extends AbstractConfigElement.Properties> {

	protected T mProperties;

	private GeneratedMethod mProviderMethod;
	private GeneratedMethod mConfiguredPredicate;

	public AbstractConfigElement(final T properties) {
		mProperties = properties;
	}

	public String getName() {
		return mProperties.mName;
	}

	public String getKey() {
		return mProperties.mKey;
	}

	public TypeName getType() {
		return mProperties.mType;
	}

	public TypeName getUnboxedType() {
		return unbox(getType());
	}

	public TypeName getDeclaredType() {
		return mProperties.mType;
	}

	public TypeName getUnboxedDeclaredType() {
		return unbox(getDeclaredType());
	}

	public TypeName getConfigClassTypeName() {
		return mProperties.mConfigClass;
	}

	public ClassName getProviderClassName() {
		return mProperties.mProviderClass;
	}

	public GeneratedMethod getProviderMethod() {
		return mProviderMethod;
	}

	public void setProviderMethod(final GeneratedMethod providerMethod) {
		mProviderMethod = providerMethod;
	}

	public GeneratedMethod getConfiguredPredicate() {
		return mConfiguredPredicate;
	}

	public void setConfiguredPredicate(final GeneratedMethod configuredPredicate) {
		mConfiguredPredicate = configuredPredicate;
	}

	protected static TypeName unbox(TypeName typeName) {
		return typeName.isBoxedPrimitive() ? typeName.unbox() : typeName;
	}

	public static <T extends AbstractConfigElement> T getConfigElementByKey(String key, List<T> configElements) {
		for (T configElement : configElements) {
			if (configElement.getKey()
			                 .equals(key)) {
				return configElement;
			}
		}
		return null;
	}

	static class Properties {

		final String    mName;
		final String    mKey;
		final TypeName  mType;
		final ClassName mProviderClass;
		final TypeName  mConfigClass;

		public Properties(final String name, final String key, final TypeName type, final ClassName providerClass, final TypeName configClass) {
			mName = name;
			mKey = key;
			mType = type;
			mProviderClass = providerClass;
			mConfigClass = configClass;
		}
	}
}
