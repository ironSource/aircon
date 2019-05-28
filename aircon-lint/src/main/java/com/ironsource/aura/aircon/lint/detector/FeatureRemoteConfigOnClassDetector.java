package com.ironsource.aura.aircon.lint.detector;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UClass;

/**
 * Created on 21/1/19.
 */
public class FeatureRemoteConfigOnClassDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("FeatureRemoteConfigOnClass", "FeatureRemoteConfig annotation used on class", "FeatureRemoteConfig annotation can only be used on interfaces");

	public FeatureRemoteConfigOnClassDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UClass node) {
		if (ConfigElementsUtils.hasFeatureRemoteConfigAnnotation(node) && !node.isInterface()) {
			report(node);
		}
	}
}
