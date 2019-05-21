package com.ironsource.aura.aircon.injection.configurators;

import android.view.View;

import com.ironsource.aura.aircon.injection.AttributeResolver;

/**
 * Created on 15/1/19.
 */
public abstract class AbstractAttributeSetter <T extends View>
		implements AttributeSetter {

	@SuppressWarnings("unchecked")
	@Override
	public final void set(final View view, final Integer attrValueInt, final String attrValueName, final AttributeResolver attributeResolver) {
		if (getViewClass() == null || getViewClass().isInstance(view)) {
			setAttr((T) view, attrValueInt, attrValueName, attributeResolver);
		}
	}

	protected abstract void setAttr(final T view, final Integer attrValueInt, final String attrValueName, final AttributeResolver attributeResolver);

	protected abstract Class<T> getViewClass();
}
