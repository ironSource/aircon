package com.ironsource.aura.aircon.lint.detector.annotation.defaultRes;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReferenceExpression;
import com.ironsource.aura.aircon.common.annotations.config.IntConfig;
import com.ironsource.aura.aircon.common.annotations.config.IntEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.JsonConfig;
import com.ironsource.aura.aircon.common.annotations.config.LongConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.TextConfig;
import com.ironsource.aura.aircon.common.annotations.config.TimeConfig;
import com.ironsource.aura.aircon.common.annotations.config.UrlConfig;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 23/1/19.
 */
public class InvalidDefaultValueResIdDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidDefaultValueResId", "Invalid default value res id", "Value must be a resource identifier of type ");

	public InvalidDefaultValueResIdDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UAnnotation node) {
		if (ConfigElementsUtils.isDefaultResAnnotation(node)) {
			visitDefaultResAnnotation(node);
		}
	}

	private void visitDefaultResAnnotation(final UAnnotation node) {
		final PsiElement defaultValueAttribute = node.findAttributeValue(null)
		                                             .getJavaPsi();
		if (!ElementUtils.isFieldReference(defaultValueAttribute)) {
			report(node);
			return;
		}

		final PsiReferenceExpression referenceExpression = (PsiReferenceExpression) defaultValueAttribute;
		final PsiField resIdField = (PsiField) referenceExpression.resolve();

		if (resIdField == null) {
			// TODO - find out why this is null
			return;
		}

		boolean invalidResource;

		final PsiAnnotation configAnnotation = ConfigElementsUtils.getConfigAnnotation((PsiField) node.getUastParent()
		                                                                                              .getJavaPsi());

		final boolean stringConfig = ElementUtils.isOneOfTypes(configAnnotation, TextConfig.class, StringConfig.class, JsonConfig.class, StringEnumConfig.class, UrlConfig.class);
		if (stringConfig && !ConfigElementsUtils.isStringResource(resIdField)) {
			report(node, "string");
			return;
		}

		invalidResource = ConfigElementsUtils.isBooleanConfigAnnotation(configAnnotation) && !ConfigElementsUtils.isBooleanResource(resIdField);
		if (invalidResource) {
			report(node, "boolean");
			return;
		}

		invalidResource = ConfigElementsUtils.isFloatConfigAnnotation(configAnnotation) && !ConfigElementsUtils.isDimenResource(resIdField);
		if (invalidResource) {
			report(node, "dimen");
			return;
		}

		final boolean integerConfig = ElementUtils.isOneOfTypes(configAnnotation, IntConfig.class, LongConfig.class, TimeConfig.class, IntEnumConfig.class);
		invalidResource = integerConfig && !ConfigElementsUtils.isIntegerResource(resIdField);
		if (invalidResource) {
			report(node, "integer");
			return;
		}

		invalidResource = ConfigElementsUtils.isColorConfigAnnotation(configAnnotation) && !ConfigElementsUtils.isColorResource(resIdField);
		if (invalidResource) {
			report(node, "color");
		}
	}

	private void report(final UAnnotation node, final String type) {
		report(node, ISSUE.getExplanation(TextFormat.TEXT) + "**" + type + "**");
	}
}
