package com.ironsource.aura.airconkt.injection.configurators;

import android.widget.Toolbar;

/**
 * Created on 15/1/19.
 */
public class TitleTextColorSetter
		extends ColorAttributeSetter<Toolbar> {

	@Override
	protected void setAttr(final Toolbar view, final int color) {
		view.setTitleTextColor(color);
	}

	@Override
	protected Class<Toolbar> getViewClass() {
		return Toolbar.class;
	}
}
