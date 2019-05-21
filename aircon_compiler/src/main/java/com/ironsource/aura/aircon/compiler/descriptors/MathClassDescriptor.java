package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

/**
 * Created on 6/3/19.
 */
public class MathClassDescriptor
		extends ClassDescriptor {

	private static final String PACKAGE = "java.lang";

	public static final ClassName CLASS_NAME = ClassName.get(PACKAGE, "Math");

	private interface StaticMethods {

		String MIN = "min";
		String MAX = "max";
	}

	private MathClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static StubClassDescriptor min(Object arg1, Object arg2) {
		return new StubClassDescriptor(staticMethodCall(CLASS_NAME, StaticMethods.MIN, arg1, arg2));
	}

	public static StubClassDescriptor max(Object arg1, Object arg2) {
		return new StubClassDescriptor(staticMethodCall(CLASS_NAME, StaticMethods.MAX, arg1, arg2));
	}
}
