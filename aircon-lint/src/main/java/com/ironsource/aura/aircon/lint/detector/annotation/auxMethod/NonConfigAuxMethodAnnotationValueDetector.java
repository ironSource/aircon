package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiElement;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 27/1/19.
 */
public class NonConfigAuxMethodAnnotationValueDetector
		extends AuxMethodAnnotationDetector {

	public static final Issue ISSUE = createErrorIssue("NonConfigAuxMethodAnnotationValue", "Non config value", "Value must be a a config field");

	public NonConfigAuxMethodAnnotationValueDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigAuxAnnotation(final UAnnotation node) {
		final PsiElement value = node.findAttributeValue(null)
		                             .getJavaPsi();

		if (!ConfigElementsUtils.isConfigFieldReference(value)) {
			report(node);
		}
	}
}
