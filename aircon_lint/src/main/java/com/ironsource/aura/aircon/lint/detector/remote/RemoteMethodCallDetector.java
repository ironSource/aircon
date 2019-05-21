package com.ironsource.aura.aircon.lint.detector.remote;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.ironsource.aura.aircon.common.utils.CommonNamingUtils;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;

import org.jetbrains.uast.UCallExpression;

import java.util.List;

/**
 * Created on 21/1/19.
 */
public class RemoteMethodCallDetector
		extends IssueDetector {

	public static final Issue ISSUE = createWarningIssue("RemoteMethodCall", "Remote method call", "Methods annotated by **@RemoteFlag** or have parameters annotated by **@RemoteParam** should not be called directly, use generated method instead");

	public RemoteMethodCallDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	public void visit(final UCallExpression node) {
		final PsiMethod method = node.resolve();
		if (method != null) {
			if (ConfigElementsUtils.isRemoteMethod(method)) {
				report(node, ISSUE.getExplanation(TextFormat.TEXT) + " - " + getGeneratedMethodSignature(method));
			}
		}
	}

	private String getGeneratedMethodSignature(final PsiMethod method) {
		final List<PsiParameter> nonRemoteParameters = ConfigElementsUtils.getNonRemoteParameters(method);

		final boolean onlyRemoteFlag = ConfigElementsUtils.hasRemoteFlagAnnotation(method) && nonRemoteParameters.size() == method.getParameterList()
		                                                                                                                          .getParametersCount();
		final StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("**");
		strBuilder.append(CommonNamingUtils.getActionMethodName(method.getName(), onlyRemoteFlag));
		strBuilder.append("(");

		for (int i = 0 ; i < nonRemoteParameters.size() ; i++) {
			final PsiParameter parameter = nonRemoteParameters.get(i);
			strBuilder.append(parameter.getType()
			                           .getPresentableText());
			strBuilder.append(" ");
			strBuilder.append(parameter.getName());

			if (i < nonRemoteParameters.size() - 1) {
				strBuilder.append(", ");
			}
		}

		strBuilder.append(")");
		strBuilder.append("**");
		return strBuilder.toString();
	}
}
