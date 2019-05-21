package com.ironsource.aura.aircon.lint.detector.annotation.config.group;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 21/1/19.
 */
public class EmptyConfigGroupValuesDetector
		extends ConfigGroupIssueDetector {

	public static final Issue ISSUE = createErrorIssue("EmptyConfigGroupValues", "Empty config group values", "Config group must have at least one config");

	public EmptyConfigGroupValuesDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigGroupAnnotation(final UAnnotation node) {
		final PsiArrayInitializerMemberValue configGroupValues = ConfigElementsUtils.getConfigGroupValuesAttribute(node.getJavaPsi());

		if (configGroupValues.getInitializers().length == 0) {
			report(node);
		}
	}
}
