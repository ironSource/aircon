package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

/**
 * Created on 11/13/2018.
 */
public class ColorIntClassDescriptor
		extends ClassDescriptor {

	public static final ClassName CLASS_NAME = ClassName.get(Consts.BASE_AIRCON_PACKAGE + ".utils", "ColorInt");

	ColorIntClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static ColorIntClassDescriptor newInstance(Object var) {
		return new ColorIntClassDescriptor(new CodeBlockBuilder().addConstructorCall(CLASS_NAME, var));
	}
}
