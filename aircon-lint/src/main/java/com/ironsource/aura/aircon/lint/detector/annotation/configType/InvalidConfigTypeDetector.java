package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiType;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UMethod;

/**
 * Created on 21/1/19.
 */
public class InvalidConfigTypeDetector
		extends ConfigTypeAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidConfigTypeDetector", "invalid config type", "invalid custom config type (interpreted by defaultValue return type), supported types: String,Integer,Float,Long and Boolean");

	private static final String PRIMITIVE_FLOAT   = "float";
	private static final String PRIMITIVE_INT     = "int";
	private static final String PRIMITIVE_LONG    = "long";
	private static final String PRIMITIVE_BOOLEAN = "boolean";

	public InvalidConfigTypeDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigTypeAnnotation(final UAnnotation node, final UClass owner) {
		final UMethod defaultValueMethod = getDefaultValueMethod(owner);
		if (defaultValueMethod == null) {
			return;
		}

		final PsiType type = defaultValueMethod.getReturnType();
		if (isOneOfTypes(type, String.class, Float.class, Integer.class, Long.class, Boolean.class)) {
			return;
		}

		final String typeName = type.getCanonicalText();
		if (typeName.equals(PRIMITIVE_FLOAT) || typeName.equals(PRIMITIVE_INT) || typeName.equals(PRIMITIVE_LONG) || typeName.equals(PRIMITIVE_BOOLEAN)) {
			return;
		}

		log(typeName);

		reportPsi(owner.getNameIdentifier());
	}

	private boolean isOneOfTypes(PsiType psiType, Class... typeClass) {
		for (Class aClass : typeClass) {
			if (psiType.getCanonicalText()
			           .equals(aClass.getCanonicalName())) {
				return true;
			}
		}

		return false;
	}
}
