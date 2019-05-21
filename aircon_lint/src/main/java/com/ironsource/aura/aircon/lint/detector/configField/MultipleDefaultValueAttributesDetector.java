package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UField;

/**
 * Created on 21/1/19.
 */
public class MultipleDefaultValueAttributesDetector
		extends DefaultValueIssueDetector {

	public static final Issue ISSUE = createErrorIssue("MultipleDefaultValueAttributes", "Multiple default values", "Config field can have only one type of default value");

	public MultipleDefaultValueAttributesDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigField(final UField node, final boolean defaultValue, final boolean defaultConfig, final boolean defaultRes) {
		if ((defaultValue && defaultConfig) || (defaultConfig && defaultRes) || (defaultValue && defaultRes)) {
			report(node);
		}
	}
}
