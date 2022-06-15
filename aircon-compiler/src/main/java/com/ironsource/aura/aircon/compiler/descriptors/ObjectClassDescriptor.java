package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.TypeName;

public class ObjectClassDescriptor extends ClassDescriptor {

	public static final TypeName CLASS_NAME = TypeName.get(Object.class);

	private ObjectClassDescriptor(CodeBlockBuilder builder) {
		super(builder);
	}

	private interface Methods {

		String TO_STRING = "toString";
	}

	public StubClassDescriptor toStringMethodCall() {
		addMethodCall(Methods.TO_STRING);
		return new StubClassDescriptor(mBuilder);
	}

	public static ObjectClassDescriptor from(Object var) {
		return new ObjectClassDescriptor(CodeBlockBuilder.of(var));
	}
}
