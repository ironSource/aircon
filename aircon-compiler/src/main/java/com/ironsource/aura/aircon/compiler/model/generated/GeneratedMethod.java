package com.ironsource.aura.aircon.compiler.model.generated;

import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * Created on 11/14/2018.
 */
public class GeneratedMethod {

	private final TypeName        mClassName;
	private final String          mName;
	private final List<Parameter> mParameters;

	public GeneratedMethod(final TypeName aClassName, final String name, final List<Parameter> parameters) {
		mClassName = aClassName;
		mName = name;
		mParameters = parameters;
	}

	public TypeName getClassName() {
		return mClassName;
	}

	public String getName() {
		return mName;
	}

	public List<Parameter> getParameters() {
		return mParameters;
	}

	public static class Parameter {

		private final String   mName;
		private final TypeName mType;

		public Parameter(final String name, final TypeName type) {
			mName = name;
			mType = type;
		}

		public String getName() {
			return mName;
		}

		public TypeName getType() {
			return mType;
		}
	}
}
