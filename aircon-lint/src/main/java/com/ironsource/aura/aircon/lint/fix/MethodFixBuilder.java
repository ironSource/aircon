package com.ironsource.aura.aircon.lint.fix;

import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.lang.jvm.JvmModifier;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UMethod;

/**
 * Created on 6/22/2019.
 */
public class MethodFixBuilder
		extends IssueFixBuilder<UMethod> {

	private static final String NAME_REGEX        = "[a-zA-Z][a-zA-Z0-9]";
	private static final String METHOD_NAME_REGEX = NAME_REGEX + "+\\(";

	public MethodFixBuilder(JavaContext context, UMethod target, String name) {
		super(context, target, name);
	}

	public MethodFixBuilder setReturnType(String returnType) {
		final String currentReturnType = mTarget.getReturnType()
		                                        .getPresentableText();
		mGroupBuilder.add(replacePattern(String.format(".*(%s) " + METHOD_NAME_REGEX, currentReturnType), returnType));
		return this;
	}

	public MethodFixBuilder setVisibility(JvmModifier visibility) {
		final JvmModifier visibilityModifier = ElementUtils.getVisibilityModifier(mTarget);
		mGroupBuilder.add(replacePattern(String.format(".*(%s).*", getModifierName(visibilityModifier)), getModifierName(visibility)));
		return this;
	}

	public MethodFixBuilder addModifier(JvmModifier modifier) {
		final String currentReturnType = mTarget.getReturnType()
		                                        .getPresentableText();
		mGroupBuilder.add(replacePattern(String.format(".*()%s " + METHOD_NAME_REGEX, currentReturnType), getModifierName(modifier) + " "));
		return this;
	}

	private String getModifierName(final JvmModifier visibility) {
		return visibility == JvmModifier.PACKAGE_LOCAL ? "" : visibility.name()
		                                                                .toLowerCase();
	}
}