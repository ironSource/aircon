package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.intellij.psi.PsiType;
import com.ironsource.aura.aircon.lint.fix.MethodFixBuilder;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UMethod;

/**
 * Created on 27/1/19.
 */
public class ConfigValidatorInvalidSignature
		extends ConfigValidatorDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidConfigValidatorSignature", "Invalid config validator signature", "Config validator return value must be boolean");

	public ConfigValidatorInvalidSignature(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigValidatorMethod(final UMethod node) {
		final PsiType returnType = node.getReturnType();
		if (!ElementUtils.isBoolean(returnType)) {
			report(node, getFix(node));
		}
	}

	private LintFix getFix(final UMethod node) {
		return new MethodFixBuilder(mContext, node, "Change return type to boolean").setReturnType("boolean")
		                                                                            .build();
	}
}
