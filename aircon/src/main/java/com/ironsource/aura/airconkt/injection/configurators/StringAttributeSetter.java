package com.ironsource.aura.airconkt.injection.configurators;

import android.view.View;

import com.ironsource.aura.airconkt.injection.AttributeResolver;

/**
 * Created on 15/1/19.
 */
public abstract class StringAttributeSetter <T extends View>
		extends AbstractAttributeSetter<T> {

	@Override
	public void setAttr(final T view, final Integer attrValueInt, final String attrValueName, final AttributeResolver attributeResolver) {
		final String str = attributeResolver.getString(attrValueName);
		if (str != null) {
			setAttr(view, str);
		}
	}

	protected abstract void setAttr(final T view, final String str);
}
