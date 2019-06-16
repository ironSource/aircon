package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.common.ConfigType;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;

abstract class ConfigTypeAnnotationIssueDetector
		extends IssueDetector {

	protected ConfigTypeAnnotationIssueDetector(final JavaContext context, final Issue issue) {
		super(context, issue);
	}

	@Override
	public final void visit(final UAnnotation node) {
		if (!ElementUtils.isOfType(node.getJavaPsi(), ConfigType.class)) {
			return;
		}

		visitConfigTypeAnnotation(node, (UClass) node.getUastParent());
	}

	protected abstract void visitConfigTypeAnnotation(final UAnnotation node, UClass owner);
}
