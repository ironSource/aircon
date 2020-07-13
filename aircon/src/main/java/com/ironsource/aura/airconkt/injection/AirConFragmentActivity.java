package com.ironsource.aura.airconkt.injection;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.ironsource.aura.airconkt.AirConKt;

/**
 * Base class for activities supporting XMl injection.
 */
public abstract class AirConFragmentActivity
		extends FragmentActivity {

	@Override
	protected void attachBaseContext(Context newBase) {
		if (AirConKt.get()
		          .isXmlInjectionEnabled()) {
			final AttributeResolver attributeResolver = getAttributeResolver();
			if (attributeResolver != null) {
				super.attachBaseContext(AirConContextWrapper.wrap(newBase, AirConKt.get()
				                                                                 .getAttrClass(), attributeResolver));
			}
		}
		else {
			super.attachBaseContext(newBase);
		}
	}

	protected AttributeResolver getAttributeResolver() {
		return AirConKt.get()
		             .getAttributeResolver();
	}

	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		View view = AirConContextWrapper.onActivityCreateView(this, name, attrs);
		if (view == null) {
			view = super.onCreateView(parent, name, context, attrs);
		}
		return view;
	}
}
