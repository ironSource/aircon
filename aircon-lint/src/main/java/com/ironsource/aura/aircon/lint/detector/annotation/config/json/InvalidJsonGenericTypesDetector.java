package com.ironsource.aura.aircon.lint.detector.annotation.config.json;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;

import org.jetbrains.uast.UAnnotation;

import java.util.Locale;

/**
 * Created on 21/1/19.
 */
public class InvalidJsonGenericTypesDetector
		extends JsonConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidEnumClass", "Invalid enum class", "incorrect number of generic types");

	private static final String DESC_FORMAT = "Type %s requires %d generic type arguments (%d defined)";

	public InvalidJsonGenericTypesDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitJsonConfigAnnotation(final UAnnotation node, final PsiClass jsonType, final PsiClass[] genericTypes) {
		final int expectedGenericTypes = jsonType.getTypeParameterList()
		                                         .getTypeParameters().length;
		final int definedGenericTypes = genericTypes.length;
		if (expectedGenericTypes != definedGenericTypes) {
			report(node, String.format(Locale.getDefault(), DESC_FORMAT, jsonType.getName(), expectedGenericTypes, definedGenericTypes));
		}
	}
}
