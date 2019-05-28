package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UField;

/**
 * Created on 23/1/19.
 */
abstract class DefaultValueIssueDetector
		extends ConfigFieldIssueDetector {

	DefaultValueIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	protected final void visitConfigField(final UField node) {
		final boolean defaultValue = ConfigElementsUtils.hasDefaultValueAttribute(node);
		final boolean defaultConfig = ConfigElementsUtils.hasDefaultConfigAnnotation(node);
		final boolean defaultRes = ConfigElementsUtils.hasDefaultResAnnotation(node);

		visitConfigField(node, defaultValue, defaultConfig, defaultRes);
	}

	protected abstract void visitConfigField(final UField node, final boolean defaultValue, final boolean defaultConfig, final boolean defaultRes);
}
