package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

import java.util.Arrays;

import static com.ironsource.aura.aircon.compiler.consts.Consts.BASE_AIRCON_PACKAGE;

/**
 * Created on 11/14/2018.
 */
public class AirConClassDescriptor
		extends ClassDescriptor {

	private static final ClassName CLASS_NAME = ClassName.get(BASE_AIRCON_PACKAGE, "AirCon");

	private interface StaticMethods {

		String GET = "get";
	}

	private interface Methods {

		String GET_CONTEXT                  = "getContext";
		String GET_JSON_CONVERTER           = "getJsonConverter";
		String GET_CONFIG_SOURCE_REPOSITORY = "getConfigSourceRepository";
		String GET_CONFIG_TYPE_RESOLVER     = "getConfigTypeResolver";
	}

	private AirConClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public ContextClassDescriptor getContext() {
		addMethodCall(Methods.GET_CONTEXT);
		return new ContextClassDescriptor(mBuilder);
	}

	public StubClassDescriptor getJsonConverter() {
		addMethodCall(Methods.GET_JSON_CONVERTER);
		return new StubClassDescriptor(mBuilder);
	}

	public ConfigSourceRepositoryClassDescriptor getConfigSourceRepository(Object... parameters) {
		addMethodCall(Methods.GET_CONFIG_SOURCE_REPOSITORY, parameters);
		return new ConfigSourceRepositoryClassDescriptor(mBuilder);
	}

	public ConfigTypeResolverClassDescriptor getConfigTypeResolver(Object rawType, Object type, Object configTypeClass) {
		addGenericMethodCall(Methods.GET_CONFIG_TYPE_RESOLVER, Arrays.asList(rawType, type), configTypeClass);
		return new ConfigTypeResolverClassDescriptor(mBuilder);
	}

	public static AirConClassDescriptor get() {
		return new AirConClassDescriptor(staticMethodCall(CLASS_NAME, StaticMethods.GET));
	}
}
