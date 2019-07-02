package com.ironsource.aura.aircon.lint.detector.annotation.config.json;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotation;
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
		final PsiAnnotation nodePsi = node.getJavaPsi();
		if (nodePsi == null || !ConfigElementsUtils.isJsonConfigAnnotation(nodePsi)) {
			return;
		}

		final PsiClass jsonType = ConfigElementsUtils.getJsonTypeAttribute(nodePsi);
		if (jsonType == null) {
			return;
		}

		final int genericTypesCount = ConfigElementsUtils.getGenericTypesCount(nodePsi);

		visitJsonConfigAnnotation(node, jsonType, genericTypesCount);
	}

	protected abstract void visitJsonConfigAnnotation(final UAnnotation node, final PsiClass jsonType, int genericTypesCount);
}
