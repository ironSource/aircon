package com.ironsource.aura.aircon.lint.detector.annotation.config.enums;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 23/1/19.
 */
abstract class EnumConfigAnnotationIssueDetector
		extends IssueDetector {

	protected EnumConfigAnnotationIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public final void visit(final UAnnotation node) {
		if (!ConfigElementsUtils.isEnumConfigAnnotation(node.getJavaPsi())) {
			return;
		}

		final PsiClass enumClass = ConfigElementsUtils.getEnumClassAttribute(node.getJavaPsi());

		visitEnumConfigAnnotation(node, enumClass);
	}

	protected abstract void visitEnumConfigAnnotation(final UAnnotation node, final PsiClass enumClass);
}
