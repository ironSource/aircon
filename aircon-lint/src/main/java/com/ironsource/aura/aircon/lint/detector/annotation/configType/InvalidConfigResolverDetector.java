package com.ironsource.aura.aircon.lint.detector.annotation.configType;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiImmediateClassType;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;

/**
 * Created on 21/1/19.
 */
public class InvalidConfigResolverDetector
		extends ConfigTypeAnnotationIssueDetector {

	public static final Issue ISSUE = createErrorIssue("InvalidConfigResolverDetector", "Invalid config resolver", "config resolver generic type doesn't match this annotation");

	private static final String CLASS_CONFIG_TYPE_RESOLVER = "ConfigTypeResolver";

	private static final int GENRIC_INDEX_ANNOTATION = 0;
	private static final int GENRIC_INDEX_RAW_TYPE   = 1;

	public InvalidConfigResolverDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	@Override
	protected void visitConfigTypeAnnotation(final UAnnotation node, final UClass owner) {
		final PsiClass resolverClass = ((PsiImmediateClassType) node.getAttributeValues()
		                                                            .get(0)
		                                                            .getExpression()
		                                                            .evaluate()).resolve();

		final PsiClassType resolverClassType = getConfigTypeResolverClassType(resolverClass);
		if (resolverClassType == null) {
			return;
		}

		final PsiType annotationType = getAnnotationType(resolverClassType);
		final PsiType rawType = getRawType(resolverClassType);

		if (!annotationType.getCanonicalText()
		                   .equals(owner.getQualifiedName())) {
			report(node);
		}
	}

	private PsiClassType getConfigTypeResolverClassType(final PsiClass resolverClass) {
		if (resolverClass != null) {
			for (final PsiClassType classInterface : resolverClass.getImplementsListTypes()) {
				if (classInterface.getClassName()
				                  .equals(CLASS_CONFIG_TYPE_RESOLVER)) {
					return classInterface;
				}
			}
		}

		return null;
	}

	private PsiType getAnnotationType(PsiClassType classType) {
		return classType.getParameters()[GENRIC_INDEX_ANNOTATION];
	}

	private PsiType getRawType(PsiClassType classType) {
		return classType.getParameters()[GENRIC_INDEX_RAW_TYPE];
	}
}
