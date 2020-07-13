package com.ironsource.aura.airconkt.injection;

import android.content.res.ColorStateList;

/**
 * Used to resolve attribute values for XMl injection.
 *
 * @see AirConContextWrapper
 * @see AirConFragmentActivity
 * @see AirConAppCompatActivity
 */
public interface AttributeResolver {

	/**
	 * Return color state list corresponding to the attribute name
	 * @param attrName attribute name
	 * @return color state list
	 */
	ColorStateList getColorStateList(String attrName);

	/**
	 * Return color corresponding to the attribute name
	 * @param attrName attribute name
	 * @return color
	 */
	Integer getColor(String attrName);

	/**
	 * Return string corresponding to the attribute name.
	 * @param attrName attribute name
	 * @return string
	 */
	String getString(String attrName);
}
