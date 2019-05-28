package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.TypeName;

/**
 * Created on 11/13/2018.
 */
public class StringClassDescriptor
		extends ClassDescriptor {

	public static final TypeName CLASS_NAME = TypeName.get(String.class);

	private interface StaticMethods {

		String VALUE_OF = "valueOf";
	}

	private interface Methods {

		String IS_EMPTY = "isEmpty";
	}

	private StringClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static StubClassDescriptor valueOf(Object value) {
		return staticMethod(StaticMethods.VALUE_OF, value);
	}

	private static StubClassDescriptor staticMethod(String method, Object... params) {
		return new StubClassDescriptor(new CodeBlockBuilder().addStaticMethodCall(CLASS_NAME, method, params));
	}

	public StubClassDescriptor isEmpty() {
		addMethodCall(Methods.IS_EMPTY);
		return new StubClassDescriptor(mBuilder);
	}

	public static StringClassDescriptor from(Object var) {
		return new StringClassDescriptor(CodeBlockBuilder.of(var));
	}
}
