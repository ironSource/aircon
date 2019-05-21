package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;

/**
 * Created on 28/4/19.
 */
public class ConfigSourceRepositoryClassDescriptor
		extends ClassDescriptor {

	private interface Methods {

		String GET_SOURCE = "getSource";
	}

	ConfigSourceRepositoryClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public ConfigSourceClassDescriptor getSource(Object... parameters) {
		addMethodCall(ConfigSourceRepositoryClassDescriptor.Methods.GET_SOURCE, parameters);
		return new ConfigSourceClassDescriptor(mBuilder);
	}
}
