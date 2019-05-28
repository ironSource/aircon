package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.common.utils.CommonNamingUtils;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UQualifiedReferenceExpression;

/**
 * Created on 21/1/19.
 */
public class ConfigFieldReferenceDetector
		extends IssueDetector {

	public static final Issue ISSUE = createWarningIssue("ConfigFieldReference", "Config field reference", "Config field should not be used directly, instead use **@RemoteFlag** or **@RemoteParam** or generated config provider");

	public ConfigFieldReferenceDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UQualifiedReferenceExpression node) {
		final PsiField referencedField = ElementUtils.getReferencedField(node.getJavaPsi());
		if (referencedField != null) {
			visitFieldReference(node, referencedField);
		}
	}

	private void visitFieldReference(final @NotNull UQualifiedReferenceExpression node, final PsiField field) {
		final PsiClass containingClass = ElementUtils.getContainingClass(node);
		if (!ConfigElementsUtils.hasFeatureRemoteConfigAnnotation(containingClass)) {
			final boolean remoteConfigField = ConfigElementsUtils.hasConfigAnnotation(field);
			if (remoteConfigField) {
				report(node, ISSUE.getExplanation(TextFormat.TEXT) + " **" + CommonNamingUtils.getProviderClassName(field.getContainingClass()
				                                                                                                         .getName()) + "**");
			}
		}
	}
}
