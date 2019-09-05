package com.ironsource.aura.aircon.injection;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.ironsource.aura.aircon.AirCon;

import java.lang.reflect.Field;


/**
 * AirConContextWrapper brings runtime color attribute override in a simple way through
 * calling {@link #wrap(Context, Class, AttributeResolver)}.
 * <p>
 * Supported attributes :
 * {@link android.R.attr#buttonTint}
 * {@link android.R.attr#drawableTint}
 * {@link android.R.attr#textColor}
 * {@link android.R.attr#background}
 * {@link android.R.attr#indeterminateTint}
 * {@link android.R.attr#backgroundTint}
 * {@link android.support.v7.appcompat.R.attr#backgroundTint}
 * {@link android.support.v7.appcompat.R.attr#titleTextColor}
 * {@link android.R.attr#textColorHighlight}
 * {@link android.R.attr#tint}
 * {@link android.R.attr#text}
 * </p>
 */
public final class AirConContextWrapper
		extends ContextWrapper {

	private static final char NAME_DELIMITER = '.';

	private final SparseArray<String> mAttrValueToNameMap;
	private final AttributeResolver   mAttributeResolver;

	private AirConLayoutInflater mInflater;

	private AirConContextWrapper(@NonNull Context base, final Class attrClass, @NonNull AttributeResolver attributeResolver) {
		super(base);
		mAttrValueToNameMap = parseAttrClass(attrClass);
		mAttributeResolver = attributeResolver;
	}

	private SparseArray<String> parseAttrClass(final Class rAttrClass) {
		final SparseArray<String> mAttrValueToNameMap;
		Field[] declaredFields = rAttrClass.getDeclaredFields();
		mAttrValueToNameMap = new SparseArray<>(declaredFields.length);
		for (Field field : declaredFields) {
			try {
				int attrValue = field.getInt(null);
				String attrName = field.getName();
				mAttrValueToNameMap.put(attrValue, attrName);
			} catch (Exception ignored) {
			}
		}
		return mAttrValueToNameMap;
	}

	@Override
	public Object getSystemService(String name) {
		if (LAYOUT_INFLATER_SERVICE.equals(name)) {
			return getLayoutInflater();
		}
		return super.getSystemService(name);
	}

	@NonNull
	private AirConLayoutInflater getLayoutInflater() {
		if (mInflater == null) {
			mInflater = new AirConLayoutInflater(LayoutInflater.from(getBaseContext()), this, mAttrValueToNameMap, mAttributeResolver);
		}
		return mInflater;
	}

	/**
	 * Call this method from {@link Activity#onCreateView(View, String, Context, AttributeSet)}
	 * so that custom views (i.e. android.support.v7.widget.Toolbar) can have their properties overridden
	 * Example:
	 * <pre>
	 * {@code
	 *
	 * public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
	 *      View view = AirConContextWrapper.onActivityCreateView(this, name, attrs);
	 *      if (view == null) {
	 *          view = super.onCreateView(parent, name, context, attrs);
	 *      }
	 *      return view;
	 * }
	 * }
	 * </pre>
	 *
	 * @param activity the activity
	 * @param name     name received in Activity.onCreateView()
	 * @param attrSet  attrSet received in Activity.onCreateView()
	 * @return view
	 */
	@Nullable
	public static View onActivityCreateView(Activity activity, String name, AttributeSet attrSet) {
		return name != null ? createView(activity, name, attrSet) : null;
	}

	@Nullable
	private static View createView(final Activity activity, final String name, final AttributeSet attrSet) {
		final int lastNameSegmentIndex = name.lastIndexOf(NAME_DELIMITER);
		if (lastNameSegmentIndex == -1) {
			return null;
		}

		final String shortName = name.substring(lastNameSegmentIndex + 1);
		final String prefix = name.substring(0, lastNameSegmentIndex) + NAME_DELIMITER;

		final AirConLayoutInflater layoutInflater = (AirConLayoutInflater) activity.getLayoutInflater();
		try {
			final View view = layoutInflater.createView(shortName, prefix, attrSet);
			if (view != null) {
				layoutInflater.onViewCreated(attrSet, view);
			}
			return view;
		} catch (Exception e) {
			if (AirCon.INSTANCE.getInitialized()) {
				AirCon.INSTANCE.getLogger()
				               .e(e.getMessage());
			}
			return null;
		}
	}

	/**
	 * Call this method from {@link android.app.Activity#attachBaseContext(Context)}.
	 * Wrap a context to intercept {@link #getSystemService(String)} calls for {@link Context#LAYOUT_INFLATER_SERVICE}
	 * so that we can provide our own {@link LayoutInflater} that will override view attributes with values from {@link AttributeResolver}
	 *
	 * @param context           context
	 * @param attrClass         R.attr.class
	 * @param attributeResolver attribute resolver
	 * @return an AirCon context wrapper
	 */
	@NonNull
	public static AirConContextWrapper wrap(@NonNull Context context, final Class attrClass, @NonNull AttributeResolver attributeResolver) {
		return new AirConContextWrapper(context, attrClass, attributeResolver);
	}
}
