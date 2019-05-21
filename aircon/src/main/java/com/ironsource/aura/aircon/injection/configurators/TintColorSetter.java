package com.ironsource.aura.aircon.injection.configurators;

import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.widget.ImageView;

/**
 * Created on 15/1/19.
 */
public class TintColorSetter
		extends ColorAttributeSetter<ImageView> {

	@Override
	protected void setAttr(final ImageView view, final int color) {
		ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color));
	}

	@Override
	protected Class<ImageView> getViewClass() {
		return ImageView.class;
	}
}
