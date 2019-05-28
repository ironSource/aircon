package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.common.annotations.ConfigMock;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UMethod;

/**
 * Created on 7/3/19.
 */
public class ConfigMockProtectionDetector
		extends AuxMethodDetector {

	public static final Issue ISSUE = createErrorIssue("ConfigMockUsage", "Config mock usage protection", "This error sole purpose is to make sure lint check fails if someone tries to build a release apk with config mocks.");

	public ConfigMockProtectionDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitAuxMethod(final UMethod node) {
		if (ElementUtils.hasAnnotation(node, ConfigMock.class)) {
			report(node);
		}
	}
}
