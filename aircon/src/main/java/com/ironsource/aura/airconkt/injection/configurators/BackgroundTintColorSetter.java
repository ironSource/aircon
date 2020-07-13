package com.ironsource.aura.airconkt.injection.configurators;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;

/**
 * Created on 15/1/19.
 */
public class BackgroundTintColorSetter
		extends ColorAttributeSetter {

	@Override
	protected void setAttr(final View view, final int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			view.setBackgroundTintList(ColorStateList.valueOf(color));
		}
		else {
			view.getBackground()
			    .setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
	}

	@Override
	protected Class<View> getViewClass() {
		return View.class;
	}
}
