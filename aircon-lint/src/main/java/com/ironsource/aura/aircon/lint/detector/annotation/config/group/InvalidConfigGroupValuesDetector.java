package com.ironsource.aura.aircon.lint.detector.annotation.config.group;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UElement;

/**
 * Created on 21/1/19.
 */
public class InvalidConfigGroupValuesDetector
		extends ConfigGroupIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidConfigGroupValues", "Invalid config group values", "Config group values must be config fields");

	public InvalidConfigGroupValuesDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigGroupAnnotation(final UAnnotation node) {
		final PsiArrayInitializerMemberValue configGroupValues = ConfigElementsUtils.getConfigGroupValuesAttribute(node.getJavaPsi());

		for (PsiAnnotationMemberValue psiAnnotationMemberValue : configGroupValues.getInitializers()) {
			if (!verifyConfigFieldReference(node, psiAnnotationMemberValue)) {
				return;
			}
		}
	}

	private boolean verifyConfigFieldReference(final UElement node, final PsiElement valueElement) {
		if (!ElementUtils.isFieldReference(valueElement)) {
			report(node);
			return false;
		}

		final PsiField configField = ElementUtils.getReferencedField(valueElement);
		if (!ConfigElementsUtils.isConfigField(configField)) {
			report(node);
			return false;
		}
		return true;
	}
}
