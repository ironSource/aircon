package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

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
		if (!ConfigElementsUtils.hasSourceAnnotation(node)) {
			report(node);
		}
	}
}
