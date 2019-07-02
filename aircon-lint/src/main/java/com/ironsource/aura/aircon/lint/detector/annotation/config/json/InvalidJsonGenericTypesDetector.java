package com.ironsource.aura.aircon.lint.detector.annotation.config.json;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiTypeParameterList;

import org.jetbrains.uast.UAnnotation;

import java.util.Locale;

/**
 * Created on 21/1/19.
 */
public class InvalidJsonGenericTypesDetector
		extends JsonConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidGenericArguments", "Invalid generic arguments", "incorrect number of generic types");

	private static final String DESC_FORMAT = "Type %s requires %d generic type arguments (%d defined)";

	public InvalidJsonGenericTypesDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitJsonConfigAnnotation(final UAnnotation node, final PsiClass jsonType, final int genericTypesCount) {
		final PsiTypeParameterList typeParameterList = jsonType.getTypeParameterList();
		final int expectedGenericTypesCount = typeParameterList != null ? typeParameterList.getTypeParameters().length : 0;
		if (expectedGenericTypesCount != genericTypesCount) {
			report(node, String.format(Locale.getDefault(), DESC_FORMAT, jsonType.getName(), expectedGenericTypesCount, genericTypesCount));
		}
	}
}
