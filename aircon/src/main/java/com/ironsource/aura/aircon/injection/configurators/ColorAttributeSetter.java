package com.ironsource.aura.aircon.injection.configurators;

import android.view.View;

import com.ironsource.aura.aircon.injection.AttributeResolver;

/**
 * Created on 15/1/19.
 */
public abstract class ColorAttributeSetter <T extends View>
		extends AbstractAttributeSetter<T> {

	@Override
	public void setAttr(final T view, final Integer attrValueInt, final String attrValueName, final AttributeResolver attributeResolver) {
		final Integer color = attributeResolver.getColor(attrValueName);
		if (color != null) {
			setAttr(view, color);
		}
	}

	protected abstract void setAttr(final T view, final int color);
}
