package com.ironsource.aura.aircon.injection.configurators;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.widget.TextView;

import com.ironsource.aura.aircon.injection.AttributeResolver;

/**
 * Created on 15/1/19.
 */
public class DrawableTintSetter
		extends AbstractAttributeSetter<TextView> {

	@Override
	protected void setAttr(final TextView view, final Integer attrValueInt, final String attrValueName, final AttributeResolver attributeResolver) {
		Integer color = attributeResolver.getColor(attrValueName);
		if (color == null) {
			//This is because there is no AppCompat version for drawableTint
			//in which case of API < 23 the system will ignore the attribute but we WANT the effect before API 23 as well
			if (attrValueInt != null) {
				TypedValue outValue = new TypedValue();
				boolean isResolved = view.getContext()
				                         .getTheme()
				                         .resolveAttribute(attrValueInt, outValue, true);
				if (isResolved) {
					color = outValue.data;
				}
			}

		}
		setAttr(view, color);
	}

	private void setAttr(final TextView view, final Integer color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			if (color != null) {
				view.setCompoundDrawableTintList(ColorStateList.valueOf(color));
			}
		}
		else {
			Drawable[] drawables = view.getCompoundDrawablesRelative();
			for (Drawable drawable : drawables) {
				if (drawable != null && color != null) {
					DrawableCompat.setTint(drawable, color);
				}
			}
		}
	}

	@Override
	protected Class<TextView> getViewClass() {
		return TextView.class;
	}
}
