package com.ironsource.aura.aircon.compiler.model.remote;

import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/3/2018.
 */
public class RemoteMethodElement {

	private final String                          mName;
	private final String                          mClassName;
	private final List<? extends VariableElement> mParameters;
	private final boolean                         mApiMethod;
	private final ConfigElement                   mFlagConfigElement;
	private final List<AbstractConfigElement>     mRemoteParameters;
	private final TypeMirror                      mReturnType;

	RemoteMethodElement(final String name, final String className, final boolean apiMethod, final ConfigElement flagConfigElement, final List<? extends VariableElement> parameters, final List<AbstractConfigElement> remoteParameters, final TypeMirror returnType) {
		mName = name;
		mClassName = className;
		mApiMethod = apiMethod;
		mFlagConfigElement = flagConfigElement;
		mParameters = parameters;
		mRemoteParameters = remoteParameters;
		mReturnType = returnType;
	}

	public String getName() {
		return mName;
	}

	public String getClassName() {
		return mClassName;
	}

	public boolean isApiMethod() {
		return mApiMethod;
	}

	public boolean isConstructor() {
		return Consts.CONSTRUCTOR_METHOD_NAME.equals(mName);
	}

	public List<? extends VariableElement> getParameters() {
		return mParameters;
	}

	public ConfigElement getFlagConfigElement() {
		return mFlagConfigElement;
	}

	public boolean hasFlagConfigElement() {
		return mFlagConfigElement != null;
	}

	public List<AbstractConfigElement> getRemoteParameters() {
		return mRemoteParameters;
	}

	public boolean hasRemoteParameters() {
		return mRemoteParameters != null && !mRemoteParameters.isEmpty();
	}

	public TypeMirror getReturnType() {
		return mReturnType;
	}

	public boolean isPredicate() {
		return TypeName.BOOLEAN.equals(TypeName.get(mReturnType));
	}

	public boolean isVoid() {
		return TypeName.VOID.equals(TypeName.get(mReturnType));
	}
}
