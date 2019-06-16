package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotated;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UNamedExpression;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Created on 21/1/19.
 */
public class NonFieldTargetDetector
		extends ConfigTypeAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("NonFieldTarget", "Non field target", "custom config type annotation must be annotated with @Target(ElementType.FIELD)");

	public NonFieldTargetDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigTypeAnnotation(final UAnnotation node, final UClass owner) {
		final List<UAnnotation> annotations = ((UAnnotated) owner).getAnnotations();
		for (UAnnotation annotation : annotations) {
			if (!isTargetAnnotation(annotation)) {
				continue;
			}

			final ElementType[] annotationTargets = getTargetValue(annotation);
			if (annotationTargets.length == 1 && annotationTargets[0] == ElementType.FIELD) {
				return;
			}
		}

		reportPsi(owner.getNameIdentifier());
	}

	private boolean isTargetAnnotation(final UAnnotation annotation) {
		return ElementUtils.isOfType(annotation.getJavaPsi(), Target.class);
	}

	private ElementType[] getTargetValue(final UAnnotation annotation) {
		final UNamedExpression attributeValue = annotation.getAttributeValues()
		                                                  .get(0);
		final Object targetValue = attributeValue.getExpression()
		                                         .evaluate();
		if (targetValue instanceof ElementType[]) {
			return (ElementType[]) targetValue;
		}
		else {
			return new ElementType[]{(ElementType) targetValue};
		}
	}
}
