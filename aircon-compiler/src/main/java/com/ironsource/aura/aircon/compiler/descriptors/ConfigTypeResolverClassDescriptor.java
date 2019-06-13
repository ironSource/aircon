package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;

public class ConfigTypeResolverClassDescriptor
		extends ClassDescriptor {

	private interface Methods {

		String IS_VALID = "isValid";
		String PROCESS  = "process";
	}

	public StubClassDescriptor isValid(Object value) {
		addMethodCall(Methods.IS_VALID, value);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor process(Object value) {
		addMethodCall(Methods.PROCESS, value);
		return new StubClassDescriptor(mBuilder);
	}

	ConfigTypeResolverClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}
}
