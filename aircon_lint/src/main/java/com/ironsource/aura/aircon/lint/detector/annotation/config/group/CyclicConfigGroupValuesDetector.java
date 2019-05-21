package com.ironsource.aura.aircon.lint.detector.annotation.config.group;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UField;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 21/1/19.
 */
public class CyclicConfigGroupValuesDetector
		extends ConfigGroupIssueDetector {

	public static final Issue ISSUE = createErrorIssue("CyclicConfigGroupValues", "Cyclic config group values", "Cyclic config group values are not allowed");

	public CyclicConfigGroupValuesDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigGroupAnnotation(final UAnnotation node) {
		final PsiField configField = ((UField) node.getUastParent()).getPsi();
		if (hasCyclicConfigGroupReference(configField, new ArrayList<>(Collections.singletonList(configField)))) {
			report(node);
		}
	}

	private boolean hasCyclicConfigGroupReference(final PsiField configField, final ArrayList<PsiField> visitedFields) {
		final PsiAnnotation configAnnotation = ConfigElementsUtils.extractConfigAnnotation(configField);
		if (!ConfigElementsUtils.isConfigGroupAnnotation(configAnnotation)) {
			return false;
		}
		final PsiArrayInitializerMemberValue configGroupValues = ConfigElementsUtils.getConfigGroupValuesAttribute(configAnnotation);

		for (PsiAnnotationMemberValue configGroupValue : configGroupValues.getInitializers()) {
			final PsiField referencedConfigField = ElementUtils.getReferencedField(configGroupValue);
			if (visitedFields.contains(referencedConfigField)) {
				return true;
			}
			visitedFields.add(referencedConfigField);
			final boolean hasCyclicConfigGroupReference = hasCyclicConfigGroupReference(referencedConfigField, visitedFields);
			if (hasCyclicConfigGroupReference) {
				return true;
			}
		}

		return false;
	}
}
