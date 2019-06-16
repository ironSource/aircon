package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UMethod;

/**
 * Created on 21/1/19.
 */
public class MissingDefaultValueAttributeDetector
		extends ConfigTypeAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("MissingDefaultValueAttribute", "Missing default value attribute", "custom config type annotations must define a \"default value\" attribute");

	private static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValue";

	public MissingDefaultValueAttributeDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigTypeAnnotation(final UAnnotation node, final UClass owner) {
		for (final UMethod method : owner.getMethods()) {
			if (method.getName()
			          .equals(ATTRIBUTE_DEFAULT_VALUE)) {
				return;
			}
		}

		reportPsi(owner.getNameIdentifier());
	}
}
