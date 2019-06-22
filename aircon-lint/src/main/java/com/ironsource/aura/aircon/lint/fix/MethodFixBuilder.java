package com.ironsource.aura.aircon.lint.fix;

import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UMethod;

/**
 * Created on 6/22/2019.
 */
public class MethodFixBuilder
		extends IssueFixBuilder<UMethod> {

	private static final String METHOD_NAME_REGEX = "[a-zA-Z][a-zA-Z0-9]+\\(";

	public MethodFixBuilder(JavaContext context, UMethod target, String name) {
		super(context, target, name);
	}

	public MethodFixBuilder setReturnType(String returnType) {
		final String currentReturnType = mTarget.getReturnType()
		                                        .getPresentableText();
		mGroupBuilder.add(replacePattern(String.format(".*(%s) " + METHOD_NAME_REGEX, currentReturnType), returnType));
		return this;
	}
}