package com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiElement;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 23/1/19.
 */
abstract class DefaultConfigAnnotationIssueDetector
		extends IssueDetector {

	protected DefaultConfigAnnotationIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public void visit(final UAnnotation node) {
		if (!ConfigElementsUtils.isDefaultConfigAnnotation(node)) {
			return;
		}

		final PsiElement defaultConfig = node.findAttributeValue(null)
		                                     .getJavaPsi();
		visitDefaultConfigAnnotation(node, defaultConfig);
	}

	protected abstract void visitDefaultConfigAnnotation(final UAnnotation node, final PsiElement defaultConfig);

}
