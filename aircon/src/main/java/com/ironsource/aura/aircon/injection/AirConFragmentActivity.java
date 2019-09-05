package com.ironsource.aura.aircon.injection;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.ironsource.aura.aircon.AirConKt;


/**
 * Base class for activities supporting XMl injection.
 */
public abstract class AirConFragmentActivity
		extends FragmentActivity {

	@Override
	protected void attachBaseContext(Context newBase) {
		if (AirConKt.INSTANCE.isXmlInjectionEnabled()) {
			final AttributeResolver attributeResolver = getAttributeResolver();
			if (attributeResolver != null) {
				super.attachBaseContext(AirConContextWrapper.wrap(newBase, AirConKt.INSTANCE.getAttrClass(), attributeResolver));
			}
		}
		else {
			super.attachBaseContext(newBase);
		}
	}

	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		if (isXmlInjectionEnabled()) {
			View view = AirConContextWrapper.onActivityCreateView(this, name, attrs);
			if (view == null) {
				view = super.onCreateView(parent, name, context, attrs);
			}
			return view;
		}
		else {
			return super.onCreateView(parent, name, context, attrs);
		}
	}

	protected AttributeResolver getAttributeResolver() {
		return AirConKt.INSTANCE.getAttributeResolver();
	}

	private boolean isXmlInjectionEnabled() {
		return AirConKt.INSTANCE.isXmlInjectionEnabled() && getAttributeResolver() != null;
	}
}
