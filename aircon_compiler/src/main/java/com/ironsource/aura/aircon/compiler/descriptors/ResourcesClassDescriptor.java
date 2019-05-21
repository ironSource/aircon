package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;

/**
 * Created on 11/14/2018.
 */
public class ResourcesClassDescriptor
		extends ClassDescriptor {

	private interface Methods {

		String GET_STRING   = "getString";
		String GET_INT      = "getInteger";
		String GET_FRACTION = "getFraction";
		String GET_BOOLEAN  = "getBoolean";
	}

	ResourcesClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public StubClassDescriptor getString(final Object resId) {
		addMethodCall(Methods.GET_STRING, resId);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor getInteger(final Object resId) {
		addMethodCall(Methods.GET_INT, resId);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor getFraction(final Object resId) {
		addMethodCall(Methods.GET_FRACTION, resId, 1, 1);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor getBoolean(final Object resId) {
		addMethodCall(Methods.GET_BOOLEAN, resId);
		return new StubClassDescriptor(mBuilder);
	}
}
