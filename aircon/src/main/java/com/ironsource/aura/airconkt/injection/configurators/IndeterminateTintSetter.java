package com.ironsource.aura.airconkt.injection.configurators;

import android.content.res.ColorStateList;
import android.os.Build;
import androidx.core.graphics.drawable.DrawableCompat;
import android.widget.ProgressBar;

/**
 * Created on 15/1/19.
 */
public class IndeterminateTintSetter
		extends ColorAttributeSetter<ProgressBar> {

	@Override
	protected void setAttr(final ProgressBar view, final int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			view.setIndeterminateTintList(ColorStateList.valueOf(color));
		}
		else {
			DrawableCompat.setTint(view.getIndeterminateDrawable(), color);
		}
	}

	@Override
	protected Class<ProgressBar> getViewClass() {
		return ProgressBar.class;
	}
}
