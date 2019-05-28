package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;

/**
 * Created on 11/14/2018.
 */
public class ContextClassDescriptor
		extends ClassDescriptor {

	private interface Methods {

		String GET_RESOURCES = "getResources";
	}

	ContextClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public ResourcesClassDescriptor getResources() {
		addMethodCall(ContextClassDescriptor.Methods.GET_RESOURCES);
		return new ResourcesClassDescriptor(mBuilder);
	}
}