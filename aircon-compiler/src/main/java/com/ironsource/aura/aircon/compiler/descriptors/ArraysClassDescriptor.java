package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

/**
 * Created on 25/12/18.
 */
public class ArraysClassDescriptor
		extends ClassDescriptor {

	private static final String PACKAGE = "java.util";

	public static final ClassName CLASS_NAME = ClassName.get(PACKAGE, "Arrays");

	private interface StaticMethods {

		String AS_LIST = "asList";
	}

	private ArraysClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static StubClassDescriptor asList(Object arr) {
		return new StubClassDescriptor(staticMethodCall(CLASS_NAME, StaticMethods.AS_LIST, arr));
	}
}
