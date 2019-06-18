package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotated;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UNamedExpression;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created on 21/1/19.
 */
public class WrongRetentionDetector
		extends ConfigTypeAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("WrongRetentionDetector", "Wrong retention", "custom config type annotation must be annotated with @Retention(RetentionPolicy.RUNTIME) since reflection is used to read it");

	public WrongRetentionDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigTypeAnnotation(final UAnnotation node, final UClass owner) {
		final List<UAnnotation> annotations = ((UAnnotated) owner).getAnnotations();
		for (UAnnotation annotation : annotations) {
			if (!isRetentionAnnotation(annotation)) {
				continue;
			}

			final RetentionPolicy retentionPolicy = getRetentionPolicyValue(annotation);
			if (retentionPolicy == RetentionPolicy.RUNTIME) {
				return;
			}
		}

		reportPsi(owner.getNameIdentifier());
	}

	private boolean isRetentionAnnotation(final UAnnotation annotation) {
		return ElementUtils.isOfType(annotation.getJavaPsi(), Retention.class);
	}

	private RetentionPolicy getRetentionPolicyValue(final UAnnotation annotation) {
		final UNamedExpression attributeValue = annotation.getAttributeValues()
		                                                  .get(0);
		return (RetentionPolicy) attributeValue.getExpression()
		                                       .evaluate();
	}
}
