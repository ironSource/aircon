package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotation;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UField;

/**
 * Created on 21/1/19.
 */
abstract class ConfigFieldIssueDetector
		extends IssueDetector {

	ConfigFieldIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public final void visit(final UField node) {
		if (!ConfigElementsUtils.hasConfigAnnotation(node)) {
			return;
		}
		final PsiAnnotation annotation = ConfigElementsUtils.extractConfigAnnotation(node.getPsi());
		if (ConfigElementsUtils.isConfigGroupAnnotation(annotation)) {
			return;
		}

		visitConfigField(node);
	}

	protected abstract void visitConfigField(final UField node);
}
