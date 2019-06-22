package com.ironsource.aura.aircon.lint.fix;

import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UAnnotation;

/**
 * Created on 6/22/2019.
 */
public class AnnotationFixBuilder
		extends IssueFixBuilder<UAnnotation> {

	public AnnotationFixBuilder(final JavaContext context, final UAnnotation target, final String name) {
		super(context, target, name);
	}

	public AnnotationFixBuilder setValue(String value) {
		replacePattern("@" + getAnnotationName() + "\\((.*)\\)", value);
		return this;
	}

	private String getAnnotationName() {
		final String qualifiedName = mTarget.getQualifiedName();
		final String[] arr = qualifiedName.split("\\.");
		return arr.length > 0 ? arr[arr.length - 1] : qualifiedName;
	}
}
