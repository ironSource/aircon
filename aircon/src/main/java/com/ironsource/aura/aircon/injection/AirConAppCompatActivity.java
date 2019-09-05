package com.ironsource.aura.aircon.injection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.ironsource.aura.aircon.AirCon;

/**
 * Base class for activities supporting XMl injection.
 */
public abstract class AirConAppCompatActivity
		extends AppCompatActivity {

	@Override
	protected void attachBaseContext(Context newBase) {
		if (isXmlInjectionEnabled()) {
			super.attachBaseContext(AirConContextWrapper.wrap(newBase, AirCon.INSTANCE.getAttrClass(), getAttributeResolver()));
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
		return AirCon.INSTANCE.getAttributeResolver();
	}

	private boolean isXmlInjectionEnabled() {
		return AirCon.INSTANCE.isXmlInjectionEnabled() && getAttributeResolver() != null;
	}
}
