package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UField;

/**
 * Created on 21/1/19.
 */
public class MultipleConfigAnnotationDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("MultipleConfigAnnotation", "Multiple config annotations", "Only one config annotation can be used for a field");

	public MultipleConfigAnnotationDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UField node) {
		final int configAnnotationsCount = ConfigElementsUtils.getConfigAnnotationsCount(node);

		if (configAnnotationsCount > 1) {
			report(node);
		}
	}
}
