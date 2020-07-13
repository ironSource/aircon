package com.ironsource.aura.airconkt.injection;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ironsource.aura.airconkt.AirConKt;

/**
 * Base class for activities supporting XMl injection.
 */
public abstract class AirConAppCompatActivity
		extends AppCompatActivity {

	@Override
	protected void attachBaseContext(Context newBase) {
		if (isXmlInjectionEnabled()) {
			super.attachBaseContext(AirConContextWrapper.wrap(newBase, AirConKt.get()
			                                                                 .getAttrClass(), getAttributeResolver()));
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

	private boolean isXmlInjectionEnabled() {
		return AirConKt.get()
		             .isXmlInjectionEnabled() && getAttributeResolver() != null;
	}


	protected AttributeResolver getAttributeResolver() {
		return AirConKt.get()
		             .getAttributeResolver();
	}
}
