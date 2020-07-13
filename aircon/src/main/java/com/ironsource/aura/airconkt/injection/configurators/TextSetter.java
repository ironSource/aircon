package com.ironsource.aura.airconkt.injection.configurators;

import android.widget.TextView;

/**
 * Created on 15/1/19.
 */
public class TextSetter
		extends StringAttributeSetter<TextView> {

	@Override
	protected void setAttr(final TextView view, final String str) {
		view.setText(str);
	}

	@Override
	protected Class<TextView> getViewClass() {
		return TextView.class;
	}
}
