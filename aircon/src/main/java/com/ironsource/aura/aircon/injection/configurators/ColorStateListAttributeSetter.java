package com.ironsource.aura.aircon.injection.configurators;

import android.content.res.ColorStateList;
import android.view.View;

import com.ironsource.aura.aircon.injection.AttributeResolver;

/**
 * Created on 15/1/19.
 */
public abstract class ColorStateListAttributeSetter <T extends View>
		extends AbstractAttributeSetter<T> {

	@Override
	public final void setAttr(final T view, final Integer attrValueInt, final String attrValueName, final AttributeResolver attributeResolver) {
		final ColorStateList colorStateList = attributeResolver.getColorStateList(attrValueName);
		if (colorStateList != null) {
			setAttr(view, colorStateList);
		}
	}

	protected abstract void setAttr(final T view, final ColorStateList colorStateList);
}
