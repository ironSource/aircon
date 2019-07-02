package com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiElement;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UField;

/**
 * Created on 23/1/19.
 */
public class WrongTypeDefaultValueConfigDetector
		extends DefaultConfigAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("WrongTypeDefaultValueConfig", "Wrong type default value config", "Value must be of same type as this config");

	public WrongTypeDefaultValueConfigDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitDefaultConfigAnnotation(final UAnnotation node, final PsiElement defaultConfig) {
		if (!ConfigElementsUtils.isConfigFieldReference(defaultConfig)) {
			return;
		}

		final UField configField = (UField) node.getUastParent();

		final String configFieldType = ConfigElementsUtils.getConfigFieldType(configField);
		if (configFieldType == null) {
			return;
		}
		final String defaultValueConfigFieldType = ConfigElementsUtils.getConfigFieldType(ElementUtils.getReferencedField(defaultConfig));

		if (!configFieldType.equals(defaultValueConfigFieldType)) {
			report(node);
		}
	}
}
