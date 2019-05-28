package com.ironsource.aura.aircon.lint.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiElement;
import com.ironsource.aura.aircon.lint.AirConUsageDetector;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UQualifiedReferenceExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 21/1/19.
 */
public abstract class IssueDetector {

	// Prevent same issue reported multiple times
	private static final Map<UElement, Issue>   REPORTED_ISSUES     = new HashMap<>();
	private static final Map<PsiElement, Issue> REPORTED_PSI_ISSUES = new HashMap<>();

	private final JavaContext mContext;
	private final Issue       mIssue;

	protected IssueDetector(final JavaContext context, final Issue issue) {
		mContext = context;
		mIssue = issue;
	}

	public void visit(final UClass node) {
		// No-op, to be overridden
	}

	public void visit(final UField node) {
		// No-op, to be overridden
	}

	public void visit(final UMethod node) {
		// No-op, to be overridden
	}

	public void visit(final UAnnotation node) {
		// No-op, to be overridden
	}

	public void visit(final UCallExpression node) {
		// No-op, to be overridden
	}

	public void visit(final UQualifiedReferenceExpression node) {
		// No-op, to be overridden
	}

	public void onClassVisited(final UClass node) {
		// No-op, to be overridden
	}

	protected void report(final UElement node) {
		report(node, mIssue.getExplanation(TextFormat.RAW));
	}

	protected void report(final UElement node, String desc) {
		if (REPORTED_ISSUES.get(node) != mIssue) {
			REPORTED_ISSUES.put(node, mIssue);
			mContext.report(mIssue, node, mContext.getLocation(node), desc);
		}
	}

	protected void reportPsi(final PsiElement node) {
		reportPsi(node, mIssue.getExplanation(TextFormat.RAW));
	}

	protected void reportPsi(final PsiElement node, String desc) {
		if (REPORTED_PSI_ISSUES.get(node) != mIssue) {
			REPORTED_PSI_ISSUES.put(node, mIssue);
			mContext.report(mIssue, node, mContext.getLocation(node), desc);
		}
	}

	protected String getIssueExplanation() {
		return mIssue.getExplanation(TextFormat.TEXT);
	}

	protected void log(String str) {
		System.out.println(getClass().getSimpleName() + ": " + str);
	}

	protected static Issue createErrorIssue(String id, String briefDesc, String explanation) {
		return createIssue(id, briefDesc, explanation, Severity.ERROR);
	}

	protected static Issue createWarningIssue(String id, String briefDesc, String explanation) {
		return createIssue(id, briefDesc, explanation, Severity.WARNING);
	}

	private static Issue createIssue(String id, String briefDesc, String explanation, Severity severity) {
		return Issue.create(id, briefDesc, explanation, Category.CORRECTNESS, 6, severity, new Implementation(AirConUsageDetector.class, Scope.JAVA_FILE_SCOPE));
	}
}
