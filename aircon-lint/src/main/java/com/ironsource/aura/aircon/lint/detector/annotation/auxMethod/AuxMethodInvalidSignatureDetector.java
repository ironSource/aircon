package com.ironsource.aura.aircon.lint.detector.annotation.auxMethod;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.lang.jvm.JvmModifier;
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
		final JvmModifier visibilityModifier = ElementUtils.getVisibilityModifier(node);
		if (visibilityModifier.equals(JvmModifier.PRIVATE) || !ElementUtils.isStatic(node)) {
			report(node);
		}
	}
}
