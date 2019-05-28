package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.common.annotations.ConfigValidator;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UMethod;

/**
 * Created on 27/1/19.
 */
abstract class ConfigValidatorDetector
		extends IssueDetector {

	protected ConfigValidatorDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public void visit(final UMethod node) {
		if (!ElementUtils.hasAnnotation(node, ConfigValidator.class)) {
			return;
		}

		visitConfigValidatorMethod(node);
	}

	protected abstract void visitConfigValidatorMethod(final UMethod node);
}
