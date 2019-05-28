package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UField;

/**
 * Created on 21/1/19.
 */
public class NonConstFieldDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("NonConstField", "Non const value", "Field value must be a const");

	public NonConstFieldDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UField node) {
		if (ConfigElementsUtils.hasConfigAnnotation(node.getPsi()) && !ElementUtils.hasConstInitializer(node)) {
			report(node);
		}
	}
}
