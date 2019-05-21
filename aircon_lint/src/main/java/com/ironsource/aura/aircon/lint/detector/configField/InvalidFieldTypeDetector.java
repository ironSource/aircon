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
public class InvalidFieldTypeDetector
		extends IssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidFieldType", "Invalid field type", "Config annotation can only be used on fields of type **String**");

	public InvalidFieldTypeDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UField node) {
		if (ConfigElementsUtils.hasConfigAnnotation(node.getPsi()) && !ElementUtils.isString(node.getType())) {
			report(
					node);
		}
	}
}
