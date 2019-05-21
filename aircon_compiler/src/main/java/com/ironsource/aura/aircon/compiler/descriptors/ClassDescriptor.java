package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

/**
 * Created on 11/14/2018.
 */
public abstract class ClassDescriptor {

	protected CodeBlockBuilder mBuilder;

	ClassDescriptor(final CodeBlockBuilder builder) {
		mBuilder = builder;
	}

	protected void addMethodCall(final String methodName, final Object... params) {
		mBuilder.add(".");
		mBuilder.addMethodCall(methodName, params);
	}

	public CodeBlock build() {
		return mBuilder.build();
	}

	protected static CodeBlockBuilder staticMethodCall(TypeName className, String methodName, Object... params) {
		return new CodeBlockBuilder().addClassQualifier(className)
		                             .addMethodCall(methodName, params);
	}

	public static StubClassDescriptor clazz(TypeName className) {
		return new StubClassDescriptor(new CodeBlockBuilder().addClassQualifier(className)
		                                                     .add("class"));
	}
}
