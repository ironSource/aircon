package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;

/**
 * Created on 16/12/18.
 */
public class MapClassDescriptor
		extends ClassDescriptor {

	private interface Methods {

		String GET = "get";
		String PUT = "put";
	}

	MapClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public StubClassDescriptor get(Object key) {
		addMethodCall(Methods.GET, key);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor put(Object key, Object value) {
		addMethodCall(Methods.PUT, key, value);
		return new StubClassDescriptor(mBuilder);
	}
}
