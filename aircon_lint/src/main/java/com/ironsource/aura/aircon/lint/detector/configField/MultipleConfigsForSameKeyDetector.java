package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UField;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 21/1/19.
 */
public class MultipleConfigsForSameKeyDetector
		extends ConfigFieldIssueDetector {

	public static final Issue ISSUE = createErrorIssue("MultipleConfigsForSameKey", "Multiple configs for same key", "");

	private static final String DESC_FORMAT = "Config field already defined for key \"%s\": %s";

	private static Set<UField> mVisitedConfigs = new HashSet<>();

	public MultipleConfigsForSameKeyDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigField(final UField node) {
		final String configKey = ElementUtils.getFieldStringConst(node);
		final UField fieldForConfigKey = getFieldForKey(configKey);

		if (fieldForConfigKey != null && !fieldForConfigKey.getName()
		                                                   .equals(node.getName())) {
			report(node, String.format(DESC_FORMAT, configKey, fieldForConfigKey.getName()));
		}
		mVisitedConfigs.add(node);
	}

	private UField getFieldForKey(String key) {
		for (UField visitedConfig : mVisitedConfigs) {
			if (ElementUtils.getFieldStringConst(visitedConfig)
			                .equals(key)) {
				return visitedConfig;
			}
		}
		return null;
	}
}
