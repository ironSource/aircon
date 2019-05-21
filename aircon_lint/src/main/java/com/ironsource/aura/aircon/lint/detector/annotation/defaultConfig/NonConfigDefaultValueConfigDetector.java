package com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiElement;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 21/1/19.
 */
public class NonConfigDefaultValueConfigDetector
		extends DefaultConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("NonConfigDefaultValueConfig", "Non config default value config", "Value must be a a config field");

	public NonConfigDefaultValueConfigDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitDefaultConfigAnnotation(final UAnnotation node, final PsiElement defaultConfig) {
		if (!ConfigElementsUtils.isConfigFieldReference(defaultConfig)) {
			report(node);
		}
	}
}
