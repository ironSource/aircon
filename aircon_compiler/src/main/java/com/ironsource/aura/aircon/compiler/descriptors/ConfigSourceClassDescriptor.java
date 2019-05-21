package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;

/**
 * Created on 18/4/19.
 */
public class ConfigSourceClassDescriptor
		extends ClassDescriptor {

	ConfigSourceClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	private interface Methods {

		String GET_FIRST_LOAD_TIME_MILLIS = "getFirstLoadTimeMillis";
		String GET                        = "get%s";
		String PUT                        = "put%s";
		String IS_CONFIGURED              = "is%sConfigured";
	}

	public StubClassDescriptor getFirstLoadTimeMillis() {
		addMethodCall(Methods.GET_FIRST_LOAD_TIME_MILLIS);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor get(String type, Object key, final Object defaultValue) {
		addMethodCall(String.format(Methods.GET, type), key, defaultValue);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor put(String type, Object key, Object value) {
		addMethodCall(String.format(Methods.PUT, type), key, value);
		return new StubClassDescriptor(mBuilder);
	}

	public StubClassDescriptor isConfigured(String type, final Object key) {
		addMethodCall(String.format(Methods.IS_CONFIGURED, type), key);
		return new StubClassDescriptor(mBuilder);
	}
}
