package com.ironsource.aura.aircon.compiler.model;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;

/**
 * Created on 21/1/19.
 */
public class ConfigAuxMethod {

	private String   mName;
	private TypeName mClass;
	private TypeName mReturnType;

	private ConfigAuxMethod(final String name, final TypeName methodClass, final TypeName returnType) {
		mName = name;
		mClass = methodClass;
		mReturnType = returnType;
	}

	public String getName() {
		return mName;
	}

	public TypeName getContainingClass() {
		return mClass;
	}

	public TypeName getReturnType() {
		return mReturnType;
	}

	public static ConfigAuxMethod from(ExecutableElement executableElement) {
		if (executableElement == null) {
			return null;
		}
		final TypeName methodClass = TypeName.get(executableElement.getEnclosingElement()
		                                                           .asType());
		return new ConfigAuxMethod(executableElement.getSimpleName()
		                                            .toString(), methodClass, TypeName.get(executableElement.getReturnType()));
	}
}
