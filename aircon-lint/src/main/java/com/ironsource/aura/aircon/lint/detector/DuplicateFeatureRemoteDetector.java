package com.ironsource.aura.aircon.lint.detector;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 21/1/19.
 */
public class DuplicateFeatureRemoteDetector
		extends IssueDetector {

	private static final String EXPLANATION_FORMAT = "FeatureRemoteConfig interface with same name is already defined: %s";

	public static final Issue ISSUE = createErrorIssue("DuplicateFeatureRemote", "FeatureRemoteConfig already defined for class with same name", "");

	private Map<String, UClass> mFeatureRemoteConfigs = new HashMap<>();

	public DuplicateFeatureRemoteDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UClass node) {
		if (!ConfigElementsUtils.hasFeatureRemoteConfigAnnotation(node)) {
			return;
		}

		final UClass featureRemoteConfig = mFeatureRemoteConfigs.get(node.getName());
		if (isAlreadyDefined(node, featureRemoteConfig)) {
			report(node, String.format(EXPLANATION_FORMAT, featureRemoteConfig.getQualifiedName()));
			return;
		}

		mFeatureRemoteConfigs.put(node.getName(), node);
	}

	private boolean isAlreadyDefined(final UClass node, final UClass featureRemoteConfig) {
		return featureRemoteConfig != null && !featureRemoteConfig.getQualifiedName()
		                                                       .equals(node.getQualifiedName());
	}
}
