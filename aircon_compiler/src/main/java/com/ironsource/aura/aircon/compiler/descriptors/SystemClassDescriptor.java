package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

/**
 * Created on 11/13/2018.
 */
public class SystemClassDescriptor
		extends ClassDescriptor {

	private static final String PACKAGE = "java.lang";

	public static final ClassName CLASS_NAME = ClassName.get(PACKAGE, "System");

	private interface StaticMethods {

		String CURRENT_TIME_MILLIS = "currentTimeMillis";
	}

	private SystemClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static StubClassDescriptor currentTimeMillis() {
		return staticMethod(StaticMethods.CURRENT_TIME_MILLIS);
	}

	private static StubClassDescriptor staticMethod(String method, Object... params) {
		return new StubClassDescriptor(staticMethodCall(CLASS_NAME, method, params));
	}
}
