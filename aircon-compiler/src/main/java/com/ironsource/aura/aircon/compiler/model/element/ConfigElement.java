package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.compiler.model.ConfigAuxMethod;
import com.ironsource.aura.aircon.compiler.model.generated.GeneratedMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/3/2018.
 */
public abstract class ConfigElement
		extends AbstractConfigElement<ConfigElement.Properties> {

	private ConfigElement mDefaultConfigValueElement;

	private GeneratedMethod mRawGetter;

	ConfigElement(Properties properties) {
		super(properties);
	}

	public String getJavadoc() {
		return mProperties.mJavadoc;
	}

	public TypeMirror getSourceTypeMirror() {
		return mProperties.mSourceTypeMirror;
	}

	public TypeMirror getSourceIdentifierTypeMirror() {
		return mProperties.mSourceIdentifierTypeMirror;
	}

	public boolean hasIdentifiableSource() {
		return mProperties.mSourceIdentifierTypeMirror != null;
	}

	@Override
	public TypeName getType() {
		return hasAdapter() ? mProperties.mAdapter.getReturnType() : super.getType();
	}

	public TypeName getRawType() {
		return mProperties.mRawType;
	}

	public TypeName getUnboxedRawType() {
		return unbox(mProperties.mRawType);
	}

	public Object getDefaultValue() {
		return mProperties.mDefaultValue;
	}

	public boolean isMutable() {
		return mProperties.mMutable;
	}

	public String getDefaultConfigValue() {
		return mProperties.mDefaultConfigValue;
	}

	public void setDefaultConfigValueElement(ConfigElement element) {
		mDefaultConfigValueElement = element;
	}

	public ConfigElement getDefaultConfigValueElement() {
		return mDefaultConfigValueElement;
	}

	public int getDefaultValueResId() {
		return mProperties.mDefaultValueResId;
	}

	public boolean hasDefaultValueResId() {
		return mProperties.mDefaultValueResId != 0;
	}

	public ConfigAuxMethod getDefaultValueProvider() {
		return mProperties.mDefaultValueProvider;
	}

	public boolean hasDefaultValueProvider() {
		return mProperties.mDefaultValueProvider != null;
	}

	public ConfigAuxMethod getAdapter() {
		return mProperties.mAdapter;
	}

	public boolean hasAdapter() {
		return mProperties.mAdapter != null;
	}

	public ConfigAuxMethod getValidator() {
		return mProperties.mValidator;
	}

	public boolean hasValidator() {
		return mProperties.mValidator != null;
	}

	public boolean hasDefaultConfigValue() {
		return mProperties.mDefaultConfigValue != null;
	}

	public ConfigAuxMethod getMock() {
		return mProperties.mMock;
	}

	public boolean hasMock() {
		return mProperties.mMock != null;
	}

	public void setRawGetter(final GeneratedMethod rawGetter) {
		mRawGetter = rawGetter;
	}

	public GeneratedMethod getRawGetter() {
		return mRawGetter;
	}

	public abstract <T, S> S accept(Visitor<T, S> visitor, final T arg);

	static class Properties
			extends AbstractConfigElement.Properties {

		private final String mJavadoc;

		private final TypeMirror mSourceTypeMirror;
		private final TypeMirror mSourceIdentifierTypeMirror;

		private final TypeName mRawType;

		private final boolean mMutable;
		private final Object  mDefaultValue;
		private final String  mDefaultConfigValue;
		private final int     mDefaultValueResId;

		private final ConfigAuxMethod mDefaultValueProvider;
		private final ConfigAuxMethod mAdapter;
		private final ConfigAuxMethod mValidator;
		private final ConfigAuxMethod mMock;

		public Properties(final String name, final String key, final ClassName providerClass, final String javadoc, final TypeMirror sourceTypeMirror, final TypeMirror sourceIdentifierTypeMirror, final TypeName type, final TypeName rawType, final Object defaultValue, final String defaultConfigValue, final int defaultValueResId, final ConfigAuxMethod defaultValueProvider, final boolean mutable, final ConfigAuxMethod adapter, final ConfigAuxMethod validator, final ConfigAuxMethod mock) {
			super(name, key, type, providerClass);
			mJavadoc = javadoc;
			mSourceTypeMirror = sourceTypeMirror;
			mSourceIdentifierTypeMirror = sourceIdentifierTypeMirror;
			mRawType = rawType;
			mDefaultValue = defaultValue;
			mMutable = mutable;
			mDefaultConfigValue = defaultConfigValue;
			mDefaultValueResId = defaultValueResId;
			mDefaultValueProvider = defaultValueProvider;
			mAdapter = adapter;
			mValidator = validator;
			mMock = mock;
		}
	}
}
