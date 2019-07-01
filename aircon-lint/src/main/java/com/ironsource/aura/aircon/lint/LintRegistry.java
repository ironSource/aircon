package com.ironsource.aura.aircon.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Contains references to all custom lint checks for config.
 */
public class LintRegistry
		extends IssueRegistry {

	private final static String ISSUE_FIELD = "ISSUE";

	@Nonnull
	@Override
	public List<Issue> getIssues() {
		final ArrayList<Issue> issues = new ArrayList<>();

		for (Class<? extends IssueDetector> detectorClass : AirConUsageDetector.DETECTORS) {
			final Issue issue = getIssue(detectorClass);
			if (issue != null) {
				issues.add(issue);
			}
		}

		return issues;
	}

	private Issue getIssue(final Class<? extends IssueDetector> detectorClass) {
		try {
			return (Issue) detectorClass.getField(ISSUE_FIELD).get(detectorClass);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int getApi() {
		return ApiKt.CURRENT_API;
	}
}
