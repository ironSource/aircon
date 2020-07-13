package com.ironsource.aura.aircon.injection;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.ironsource.aura.aircon.injection.configurators.AttributeSetter;
import com.ironsource.aura.aircon.injection.configurators.AttributeSetterFactory;

/**
 * Created on 14/1/19.
 */
final class AirConLayoutInflater
		extends LayoutInflater {

	private static final String TAG = "AirConLayoutInflater";

	/**
	 * see com.android.internal.policy.PhoneLayoutInflater#sClassPrefixList
	 */
	private static final String[] sClassPrefixList = {"android.widget.", "android.webkit.", "android.app."};

	private final SparseArray<String> mAttrValueToNameMap;
	private final AttributeResolver   mAttributeResolver;

	AirConLayoutInflater(LayoutInflater layoutInflater, Context newContext, final SparseArray<String> attrValueToNameMap, @NonNull AttributeResolver attributeResolver) {
		super(layoutInflater, newContext);
		mAttrValueToNameMap = attrValueToNameMap;
		mAttributeResolver = attributeResolver;
	}

	@Override
	public LayoutInflater cloneInContext(Context newContext) {
		return new AirConLayoutInflater(this, newContext, mAttrValueToNameMap, mAttributeResolver);
	}

	@Override
	public void setFactory(Factory factory) {
		if (factory instanceof FactoryWrapper) {
			super.setFactory(factory);
		}
		else {
			super.setFactory(new FactoryWrapper(factory));
		}
	}

	@Override
	public void setFactory2(Factory2 factory) {
		if (factory instanceof Factory2Wrapper) {
			super.setFactory2(factory);
		}
		else {
			super.setFactory2(new Factory2Wrapper(factory));
		}
	}

	@Override
	protected View onCreateView(View parent, String name, AttributeSet attrs) throws
			ClassNotFoundException {
		final View view = super.onCreateView(parent, name, attrs);
		if (view != null) {
			onViewCreated(attrs, view);
		}
		return view;
	}

	void onViewCreated(AttributeSet attrs, View view) {
		if (view == null || attrs == null) {
			return;
		}
		for (int i = 0 ; i < attrs.getAttributeCount() ; i++) {
			final int attrName = attrs.getAttributeNameResource(i);
			final Integer attrValueInt = getAttrValueInt(attrs.getAttributeValue(i));
			final String attrValueName = getAttrValueName(attrValueInt);
			if (attrValueName == null) {
				continue;
			}

			final AttributeSetter attributeSetter = AttributeSetterFactory.create(attrName);
			if (attributeSetter != null) {
				attributeSetter.set(view, attrValueInt, attrValueName, mAttributeResolver);
			}
		}
	}

	@Nullable
	private Integer getAttrValueInt(String attrValue) {
		if (attrValue == null || attrValue.length() <= 1) {
			return null;
		}
		try {
			return Integer.parseInt(attrValue.substring(1));
		} catch (Exception e) {
			return null;
		}
	}

	@Nullable
	private String getAttrValueName(@Nullable Integer attrValueInt) {
		return attrValueInt != null ? mAttrValueToNameMap.get(attrValueInt) : null;
	}

	@Override
	protected View onCreateView(String name, AttributeSet attrs) throws
			ClassNotFoundException {
		View view = null;
		// This mimics the {@code PhoneLayoutInflater} in the way it tries to inflate the base
		// classes.
		for (String prefix : sClassPrefixList) {
			try {
				view = createView(name, prefix, attrs);
				if (view != null) {
					break;
				}
			} catch (ClassNotFoundException ignored) {
			}
		}
		if (view == null) {
			view = super.onCreateView(name, attrs);
		}
		return view;
	}

	/**
	 * Wraps {@link Factory2} so that views created by the original factory will still reach
	 * {@link AirConLayoutInflater#onViewCreated(AttributeSet, View)}
	 */
	private class Factory2Wrapper
			implements Factory2 {

		private final Factory2 mFactory2;

		Factory2Wrapper(Factory2 factory2) {
			mFactory2 = factory2;
		}

		@Override
		public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
			View view = mFactory2.onCreateView(parent, name, context, attrs);
			onViewCreated(attrs, view);
			return view;
		}

		@Override
		public View onCreateView(String name, Context context, AttributeSet attrs) {
			View view = mFactory2.onCreateView(name, context, attrs);
			onViewCreated(attrs, view);
			return view;
		}
	}


	/**
	 * Wraps {@link Factory} so that views created by the original factory will still reach
	 * {@link AirConLayoutInflater#onViewCreated(AttributeSet, View)}
	 */
	private class FactoryWrapper
			implements Factory {

		private final Factory mFactory;

		FactoryWrapper(Factory factory) {
			mFactory = factory;
		}

		@Override
		public View onCreateView(String name, Context context, AttributeSet attrs) {
			View view = mFactory.onCreateView(name, context, attrs);
			onViewCreated(attrs, view);
			return view;
		}
	}
}
