package com.ironsource.aura.aircon.lint.detector.annotation.config.enums;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 21/1/19.
 */
public class InvalidEnumDefaultValueDetector
		extends EnumConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidEnumDefaultValue", "Invalid enum default value", "Enum config default value must be one of enum values");

	public InvalidEnumDefaultValueDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitEnumConfigAnnotation(final UAnnotation node, final PsiClass enumClass) {
		final List<Object> remoteValues = new ArrayList<>();

		for (PsiField psiField : enumClass.getFields()) {
			final Object remoteValue = ConfigElementsUtils.getRemoteValue(psiField);
			if (remoteValue == null) {
				return;
			}
			remoteValues.add(remoteValue);
		}

		final Object defaultValue = ConfigElementsUtils.getDefaultValueAttribute(node.getJavaPsi());

		if (!remoteValues.contains(defaultValue)) {
			report(node, getIssueExplanation() + " " + Arrays.toString(remoteValues.toArray()));
		}
	}
}
