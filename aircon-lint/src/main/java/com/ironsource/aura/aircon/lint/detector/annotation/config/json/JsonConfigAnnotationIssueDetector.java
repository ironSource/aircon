package com.ironsource.aura.aircon.lint.detector.annotation.config.json;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 23/1/19.
 */
abstract class JsonConfigAnnotationIssueDetector
		extends IssueDetector {

	protected JsonConfigAnnotationIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public final void visit(final UAnnotation node) {
		if (!ConfigElementsUtils.isJsonConfigAnnotation(node.getJavaPsi())) {
			return;
		}

		final PsiClass jsonType = ConfigElementsUtils.getJsonTypeAttribute(node.getJavaPsi());
		final PsiClass[] genericTypes = ConfigElementsUtils.getGenericTypesAttribute(node.getJavaPsi());

		visitJsonConfigAnnotation(node, jsonType, genericTypes);
	}

	protected abstract void visitJsonConfigAnnotation(final UAnnotation node, final PsiClass jsonType, PsiClass[] genericTypes);
}
