package com.ironsource.aura.aircon.lint.detector.remote;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiType;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.java.JavaUParameter;

/**
 * Created on 21/1/19.
 */
public class WrongConfigTypeDetector
		extends IssueDetector {

	public final static Issue ISSUE = createErrorIssue("WrongConfigType", "Wrong config type", "Value must be config field of correct type");

	private static final String WRONG_CONFIG_TYPE_ERROR_MSG_FORMAT = "%s (**Expected:** %s, **Found:** %s)";
	private static final String MISSING_CONFIG_ANNOTATION          = "None";

	public WrongConfigTypeDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UAnnotation node) {
		if (ConfigElementsUtils.isRemoteFlagAnnotation(node)) {
			visitRemoteFlagAnnotation(node);
		}
		else if (ConfigElementsUtils.isRemoteParamAnnotation(node)) {
			visitRemoteParamAnnotation(node);
		}
	}

	private void visitRemoteFlagAnnotation(final @NotNull UAnnotation node) {
		visitRemoteAnnotation(node, PsiType.BOOLEAN);
	}

	private void visitRemoteParamAnnotation(final UAnnotation node) {
		final JavaUParameter remoteParameter = ElementUtils.getAnnotationOwner(node);
		visitRemoteAnnotation(node, remoteParameter.getType());
	}

	private void visitRemoteAnnotation(final UAnnotation node, PsiType expectedConfigType) {
		final UExpression value = ConfigElementsUtils.getRemoteAnnotationConfigValue(node);
		if (!ElementUtils.isFieldReference(value)) {
			report(node);
			return;
		}

		final String expectedTypeName = ElementUtils.getQualifiedName(expectedConfigType);
		final String configAnnotationTypeName = ConfigElementsUtils.getRemoteAnnotationReferencedConfigAnnotationType(node);

		if (!expectedTypeName.equalsIgnoreCase(configAnnotationTypeName)) {
			report(node, getWrongConfigTypeErrorMessageFormat(expectedTypeName, configAnnotationTypeName));
		}
	}

	private String getWrongConfigTypeErrorMessageFormat(final String expectedTypeName, final String configAnnotationTypeName) {
		return String.format(WRONG_CONFIG_TYPE_ERROR_MSG_FORMAT, ISSUE.getExplanation(TextFormat.TEXT), expectedTypeName, configAnnotationTypeName != null ? configAnnotationTypeName : MISSING_CONFIG_ANNOTATION);
	}
}
