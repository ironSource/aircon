package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 27/1/19.
 */
abstract class AuxMethodAnnotationDetector
		extends IssueDetector {

	protected AuxMethodAnnotationDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public void visit(final UAnnotation node) {
		if (!ConfigElementsUtils.isConfigAuxAnnotation(node)) {
			return;
		}

		visitConfigAuxAnnotation(node);
	}

	protected abstract void visitConfigAuxAnnotation(final UAnnotation node);
}
