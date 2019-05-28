package com.ironsource.aura.aircon.lint.detector.annotation.config.enums;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 21/1/19.
 */
public class InvalidEnumClassDetector
		extends EnumConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidEnumClass", "Invalid enum class", "enumClass must be an enum with fields annotated by one of the @RemoteValue annotations");

	public InvalidEnumClassDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitEnumConfigAnnotation(final UAnnotation node, final PsiClass enumClass) {
		final PsiField[] fields = enumClass.getFields();

		if (fields.length == 0) {
			report(node);
			return;
		}

		for (PsiField psiField : fields) {
			if (ElementUtils.isEnumConst(psiField) && !ConfigElementsUtils.hasRemoteValueAnnotation(psiField)) {
				report(node);
				return;
			}
		}
	}
}
