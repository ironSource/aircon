package com.ironsource.aura.aircon.injection.configurators;

import android.view.View;

/**
 * Created on 15/1/19.
 */
public class BackgroundColorSetter
		extends ColorAttributeSetter {

	@Override
	protected void setAttr(final View view, final int color) {
		view.setBackgroundColor(color);
	}

	@Override
	protected Class<View> getViewClass() {
		return View.class;
	}
}
