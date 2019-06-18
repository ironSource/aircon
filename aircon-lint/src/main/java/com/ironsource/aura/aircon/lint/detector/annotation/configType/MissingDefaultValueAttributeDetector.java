package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;

/**
 * Created on 21/1/19.
 */
public class MissingDefaultValueAttributeDetector
		extends ConfigTypeAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("MissingDefaultValueAttribute", "Missing default value attribute", "custom config type annotations must define a \"defaultValue()\" attribute");

	public MissingDefaultValueAttributeDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigTypeAnnotation(final UAnnotation node, final UClass owner) {
		if (getDefaultValueMethod(owner) == null) {
			reportPsi(owner.getNameIdentifier());
		}
	}
}
