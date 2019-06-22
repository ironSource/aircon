package com.ironsource.aura.aircon.lint.detector.annotation.defaultRes;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.fix.AnnotationFixBuilder;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 23/1/19.
 */
public class InvalidDefaultValueResIdDetector
		extends IssueDetector {

	public static final  Issue  ISSUE                   = createErrorIssue("InvalidDefaultValueResId", "Invalid default value res id", "Value must be a resource identifier of type ");
	private static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValue";
	private static final String R_STRING                = "R.string";
	private static final String R_INTEGER               = "R.integer";
	private static final String R_DIMEN                 = "R.dimen";
	private static final String R_BOOLEAN               = "R.bool";
	private static final String R_COLOR                 = "R.color";

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

		final PsiField configField = (PsiField) node.getUastParent()
		                                            .getJavaPsi();
		if (configField == null) {
			return;
		}
		final PsiAnnotation configAnnotation = ConfigElementsUtils.getConfigAnnotation(configField);

		final PsiType configType = getConfigType(configAnnotation);

		if (configType == null) {
			return;
		}

		if (ConfigElementsUtils.isColorConfigAnnotation(configAnnotation)) {
			if (!isColorResource(defaultValueAttribute)) {
				report(node, "color", R_COLOR);
			}
			return;
		}

		if (ElementUtils.isString(configType) && !isStringResource(defaultValueAttribute)) {
			report(node, "string", R_STRING);
			return;
		}

		if (ElementUtils.isBoolean(configType) && !isBooleanResource(defaultValueAttribute)) {
			report(node, "boolean", R_BOOLEAN);
			return;
		}

		if (ElementUtils.isFloat(configType) && !isDimenResource(defaultValueAttribute)) {
			report(node, "dimen", R_DIMEN);
			return;
		}

		if ((ElementUtils.isInt(configType) || ElementUtils.isLong(configType)) && !isIntegerResource(defaultValueAttribute)) {
			report(node, "integer", R_INTEGER);
			return;
		}
	}

	private PsiType getConfigType(final PsiAnnotation configAnnotation) {
		final PsiClass configAnnotationClass = ElementUtils.getAnnotationDeclarationClass(configAnnotation);
		final PsiMethod[] methods = configAnnotationClass.getMethods();
		for (PsiMethod method : methods) {
			if (method.getName()
			          .equals(ATTRIBUTE_DEFAULT_VALUE)) {
				return method.getReturnType();
			}
		}

		return null;
	}

	private boolean isStringResource(PsiElement defaultValueElement) {
		return defaultValueElement.getText()
		                          .contains(R_STRING);
	}

	private boolean isIntegerResource(PsiElement defaultValueElement) {
		return defaultValueElement.getText()
		                          .contains(R_INTEGER);
	}

	private boolean isDimenResource(PsiElement defaultValueElement) {
		return defaultValueElement.getText()
		                          .contains(R_DIMEN);
	}

	private boolean isBooleanResource(PsiElement defaultValueElement) {
		return defaultValueElement.getText()
		                          .contains(R_BOOLEAN);
	}

	private boolean isColorResource(PsiElement defaultValueElement) {
		return defaultValueElement.getText()
		                          .contains(R_COLOR);
	}

	private void report(final UAnnotation node, final String type, final String rClass) {
		super.report(node, ISSUE.getExplanation(TextFormat.TEXT) + "**" + type + "**", getFix(node, type, rClass));
	}

	private LintFix getFix(final UAnnotation node, String type, final String rClass) {
		return new AnnotationFixBuilder(mContext, node, "Change to " + type + " resource").setValue(rClass + ".")
		                                                                                  .build();
	}
}
