package com.ironsource.aura.airconkt.injection.configurators;

import android.content.res.ColorStateList;
import androidx.core.widget.CompoundButtonCompat;
import android.widget.CompoundButton;

/**
 * Created on 15/1/19.
 */
public class ButtonTintSetter
		extends ColorStateListAttributeSetter<CompoundButton> {

	@Override
	protected void setAttr(final CompoundButton view, final ColorStateList colorStateList) {
		CompoundButtonCompat.setButtonTintList(view, colorStateList);
	}

	@Override
	protected Class<CompoundButton> getViewClass() {
		return CompoundButton.class;
	}
}
