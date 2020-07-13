package com.ironsource.aura.aircon.injection.configurators;

import android.content.res.ColorStateList;
import androidx.core.view.ViewCompat;
import android.view.View;

/**
 * Created on 15/1/19.
 */
public class CompatBackgroundTintColorSetter
		extends ColorAttributeSetter {

	@Override
	protected void setAttr(final View view, final int color) {
		ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(color));
	}

	@Override
	protected Class<View> getViewClass() {
		return View.class;
	}
}
