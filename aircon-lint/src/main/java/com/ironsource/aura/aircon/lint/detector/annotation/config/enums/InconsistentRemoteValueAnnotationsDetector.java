package com.ironsource.aura.aircon.lint.detector.annotation.config.enums;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UClass;

/**
 * Created on 21/1/19.
 */
public class InconsistentRemoteValueAnnotationsDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("InconsistentRemoteValueAnnotations", "Inconsistent remove value annotations", "All enum fields must contain @RemoteValue annotation of same type");

	public InconsistentRemoteValueAnnotationsDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UClass node) {
		if (!node.isEnum()) {
			return;
		}

		final PsiField[] fields = node.getPsi()
		                              .getFields();
		PsiAnnotation expectedRemoteValueAnnotation = null;
		for (PsiField field : fields) {
			if (!ElementUtils.isEnumConst(field)) {
				continue;
			}
			final PsiAnnotation remoteValueAnnotation = ConfigElementsUtils.getRemoteValueAnnotation(field);
			if (remoteValueAnnotation != null) {
				expectedRemoteValueAnnotation = remoteValueAnnotation;
				break;
			}
		}

		final boolean configValuesEnum = expectedRemoteValueAnnotation != null;

		if (!configValuesEnum) {
			return;
		}

		for (PsiField field : fields) {
			if (!ElementUtils.isEnumConst(field)) {
				continue;
			}
			final PsiAnnotation remoteValueAnnotation = ConfigElementsUtils.getRemoteValueAnnotation(field);
			final String expectedName = expectedRemoteValueAnnotation.getQualifiedName();
			if (remoteValueAnnotation == null || !expectedName.equals(remoteValueAnnotation.getQualifiedName())) {
				reportPsi((PsiElement) node);
				return;
			}
		}
	}
}
