package com.ironsource.aura.aircon.lint.detector.annotation.config.group;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 21/1/19.
 */
abstract class ConfigGroupIssueDetector
		extends IssueDetector {

	ConfigGroupIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public final void visit(final UAnnotation node) {
		if (!ConfigElementsUtils.isConfigGroupAnnotation(node)) {
			return;
		}

		visitConfigGroupAnnotation(node);
	}

	protected abstract void visitConfigGroupAnnotation(final UAnnotation node);
}
