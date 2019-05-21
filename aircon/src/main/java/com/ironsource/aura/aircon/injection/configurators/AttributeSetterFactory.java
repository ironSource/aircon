package com.ironsource.aura.aircon.injection.configurators;

/**
 * Created on 14/1/19.
 */
public class AttributeSetterFactory {

	public static AttributeSetter create(final int attrName) {
		if (attrName == android.R.attr.buttonTint || attrName == android.support.v7.appcompat.R.attr.buttonTint) {
			return new ButtonTintSetter();
		}
		else if (attrName == android.R.attr.drawableTint) {
			return new DrawableTintSetter();
		}
		else if (attrName == android.R.attr.textColor) {
			return new TextColorSetter();
		}
		else if (attrName == android.R.attr.background) {
			return new BackgroundColorSetter();
		}
		else if (attrName == android.R.attr.indeterminateTint) {
			return new IndeterminateTintSetter();
		}
		else if (attrName == android.R.attr.backgroundTint) {
			return new BackgroundTintColorSetter();
		}
		else if (attrName == android.support.v7.appcompat.R.attr.backgroundTint) {
			return new CompatBackgroundTintColorSetter();
		}
		else if (attrName == android.R.attr.textColorHighlight) {
			return new TextHighlightColorSetter();
		}
		else if (attrName == android.R.attr.tint) {
			return new TintColorSetter();
		}
		else if (attrName == android.support.v7.appcompat.R.attr.titleTextColor) {
			return new TitleTextColorSetter();
		}
		else if (attrName == android.R.attr.text) {
			return new TextSetter();
		}

		return null;
	}
}
