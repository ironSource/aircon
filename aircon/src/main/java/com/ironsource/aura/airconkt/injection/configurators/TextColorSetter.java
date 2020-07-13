package com.ironsource.aura.airconkt.injection.configurators;

import android.widget.TextView;

/**
 * Created on 15/1/19.
 */
public class TextColorSetter
		extends ColorAttributeSetter<TextView> {

	@Override
	protected void setAttr(final TextView view, final int color) {
		view.setTextColor(color);
	}

	@Override
	protected Class<TextView> getViewClass() {
		return TextView.class;
	}
}
