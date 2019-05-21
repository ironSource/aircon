package com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UField;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 23/1/19.
 */
public class CyclicDefaultValueConfigDetector
		extends DefaultConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("CyclicDefaultValueConfig", "Cyclic default value config", "Cyclic config references are not allowed");

	public CyclicDefaultValueConfigDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitDefaultConfigAnnotation(final UAnnotation node, final PsiElement defaultConfig) {
		final UField configField = (UField) node.getUastParent();

		final boolean cyclicDefaultValueConfigReference = hasCyclicDefaultValueConfigReference(configField, new ArrayList<>(Collections.singletonList(configField.getPsi())));
		if (cyclicDefaultValueConfigReference) {
			report(node);
		}
	}

	private boolean hasCyclicDefaultValueConfigReference(final PsiField configField, final ArrayList<PsiField> visitedFields) {
		if (!ConfigElementsUtils.hasDefaultConfigAnnotation(configField)) {
			return false;
		}
		final PsiAnnotation defaultConfigAnnotation = ConfigElementsUtils.getDefaultConfigAnnotation(configField);
		final PsiElement defaultValueAttribute = defaultConfigAnnotation.findAttributeValue(null);
		final PsiField referencedDefaultConfigValue = ElementUtils.getReferencedField(defaultValueAttribute);
		if (visitedFields.contains(referencedDefaultConfigValue)) {
			return true;
		}

		visitedFields.add(referencedDefaultConfigValue);
		return hasCyclicDefaultValueConfigReference(referencedDefaultConfigValue, visitedFields);
	}
}
