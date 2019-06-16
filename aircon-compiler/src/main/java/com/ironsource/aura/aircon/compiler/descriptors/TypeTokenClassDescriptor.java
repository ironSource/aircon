package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * Created on 6/14/2019.
 */
public class TypeTokenClassDescriptor
		extends ClassDescriptor {

	private static final String PACKAGE = "com.google.gson.reflect";

	public static final ClassName CLASS_NAME = ClassName.get(PACKAGE, "TypeToken");

	private interface Methods {

		String GET_TYPE = "getType";
	}

	private TypeTokenClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public StubClassDescriptor getType() {
		addMethodCall(Methods.GET_TYPE);
		return new StubClassDescriptor(mBuilder);
	}

	public static TypeTokenClassDescriptor newInstance(TypeName typeName) {
		return new TypeTokenClassDescriptor(new CodeBlockBuilder().addConstructorCall(ParameterizedTypeName.get(CLASS_NAME, typeName))
		                                                          .add("{}"));
	}
}
