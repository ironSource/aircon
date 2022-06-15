package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.intellij.lang.jvm.JvmModifier;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiModifier;
import com.ironsource.aura.aircon.lint.fix.MethodFixBuilder;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UMethod;

/**
 * Created on 27/1/19.
 */
public class AuxMethodInvalidSignatureDetector
		extends AuxMethodDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidConfigMethodSignature", "Invalid config method signature", "Config method must be static and non-private");

	public AuxMethodInvalidSignatureDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitAuxMethod(final UMethod node) {
		final String visibilityModifier = ElementUtils.getVisibilityModifier(node);
		final boolean staticMethod = ElementUtils.isStatic(node);
		if (visibilityModifier.equals(PsiModifier.PRIVATE) || !staticMethod) {
			report(node, getFix(node, staticMethod));
		}
	}

	private LintFix getFix(final UMethod node, final boolean staticMethod) {
		final LintFix.GroupBuilder builder = LintFix.create()
		                                            .group();
		builder.add(getFix(node, staticMethod, JvmModifier.PACKAGE_LOCAL));
		builder.add(getFix(node, staticMethod, JvmModifier.PUBLIC));
		return builder.build();
	}

	private LintFix getFix(final UMethod node, final boolean staticMethod, JvmModifier visibilityModifier) {
		final MethodFixBuilder methodFixBuilder = new MethodFixBuilder(mContext, node, "Change to " + getModifierName(visibilityModifier) + " static method").setVisibility(visibilityModifier);
		if (!staticMethod) {
			methodFixBuilder.addModifier(JvmModifier.STATIC);
		}
		return methodFixBuilder.build();
	}

	private String getModifierName(final JvmModifier visibility) {
		return visibility == JvmModifier.PACKAGE_LOCAL ? "package-local" : visibility.name()
		                                                                             .toLowerCase();
	}
}
