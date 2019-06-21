package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.ironsource.aura.aircon.common.annotations.Source;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UField;

/**
 * Created on 21/1/19.
 */
public class MissingSourceDetector
		extends ConfigFieldIssueDetector {

	public static final Issue ISSUE = createErrorIssue("MissingSource", "Missing source", "Config field must specify source using @Source annotation");

	public MissingSourceDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigField(final UField node) {
		if (!ConfigElementsUtils.hasSourceAnnotation(node) && !ConfigElementsUtils.hasSourceAnnotation(node.getContainingClass())) {
			report(node, createFix(node));
		}
	}

	private LintFix createFix(final UField node) {
		final LintFix.GroupBuilder groupBuilder = LintFix.create()
		                                                 .group();
		groupBuilder.add(addSourceAnnotationFix(node, "field"));
		groupBuilder.add(addSourceAnnotationFix(node.getUastParent(), "feature interface"));
		return groupBuilder.build();
	}

	private LintFix addSourceAnnotationFix(UElement target, String targetName) {
		return addAnnotationFix(target, targetName, Source.class, "SomeConfigSource.class");
	}
}
