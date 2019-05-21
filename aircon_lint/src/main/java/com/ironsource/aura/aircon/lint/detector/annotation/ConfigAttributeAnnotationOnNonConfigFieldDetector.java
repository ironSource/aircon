package com.ironsource.aura.aircon.lint.detector.annotation;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 23/1/19.
 */
public class ConfigAttributeAnnotationOnNonConfigFieldDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("ConfigAttributeAnnotationOnNonConfigField", "Config attribute annotation on non-config field", "can only be applied to a config field");

	public ConfigAttributeAnnotationOnNonConfigFieldDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UAnnotation node) {
		if (!ConfigElementsUtils.isConfigAttributeAnnotation(node)) {
			return;
		}
		final PsiField field = (PsiField) node.getUastParent()
		                                      .getJavaPsi();
		if (!ConfigElementsUtils.isConfigField(field) || ConfigElementsUtils.isConfigGroupField(field)) {
			report(node, "**@" + getSimpleName(node) + "** " + ISSUE.getExplanation(TextFormat.TEXT));
		}
	}

	private String getSimpleName(UAnnotation node) {
		final String[] arr = node.getQualifiedName()
		                         .split("\\.");
		return arr[arr.length - 1];
	}
}
