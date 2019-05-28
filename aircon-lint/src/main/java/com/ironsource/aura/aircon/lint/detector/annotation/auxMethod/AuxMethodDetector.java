package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UMethod;

/**
 * Created on 27/1/19.
 */
abstract class AuxMethodDetector
		extends IssueDetector {

	protected AuxMethodDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public void visit(final UMethod node) {
		if (!ConfigElementsUtils.isConfigAuxMethod(node)) {
			return;
		}
		visitAuxMethod(node);
	}

	protected abstract void visitAuxMethod(final UMethod node);
}
