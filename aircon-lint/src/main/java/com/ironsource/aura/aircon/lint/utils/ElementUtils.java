package com.ironsource.aura.aircon.lint.utils;

import com.intellij.lang.jvm.JvmModifier;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.java.JavaUCodeBlockExpression;
import org.jetbrains.uast.java.JavaUSimpleNameReferenceExpression;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 23/1/19.
 */
public class ElementUtils {

	private static final Object JAVA_LANG_PACKAGE = "java.lang.";

	private static final String STRING_TYPE  = "String";
	private static final String BOOLEAN_TYPE = "boolean";
	private static final String FLOAT_TYPE   = "float";
	private static final String INT_TYPE     = "int";
	private static final String LONG_TYPE    = "long";

	public static boolean isEnumConst(PsiField psiField) {
		final String fieldType = psiField.getType()
		                                 .getCanonicalText();
		final PsiClass containingClass = psiField.getContainingClass();
		return containingClass.isEnum() && fieldType.equals(containingClass.getQualifiedName());
	}

	public static PsiField getReferencedField(final PsiElement element) {
		if (!(element instanceof PsiReferenceExpression)) {
			return null;
		}
		final PsiElement referenced = ((PsiReferenceExpression) element).resolve();
		return referenced instanceof PsiField ? (PsiField) referenced : null;
	}

	public static JvmModifier getVisibilityModifier(UMethod method) {
		for (JvmModifier jvmModifier : method.getModifiers()) {
			switch (jvmModifier) {
				case PUBLIC:
				case PACKAGE_LOCAL:
				case PROTECTED:
				case PRIVATE:
					return jvmModifier;
			}
		}
		return method.getContainingClass()
		             .isInterface() ? JvmModifier.PUBLIC : JvmModifier.PACKAGE_LOCAL;
	}

	public static boolean isOfType(final PsiAnnotation annotation, final Class<? extends Annotation> configClass) {
		return annotation != null && configClass.getCanonicalName()
		                                        .equals(annotation.getQualifiedName());
	}

	public static boolean isOneOfTypes(final PsiAnnotation annotation, final Class<? extends Annotation>... configClasses) {
		for (Class<? extends Annotation> configClass : configClasses) {
			if (isOfType(annotation, configClass)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isString(final @NotNull PsiType type) {
		return type.getPresentableText()
		           .equals(STRING_TYPE);
	}

	public static boolean isBoolean(final @NotNull PsiType type) {
		return type.getPresentableText()
		           .equals(BOOLEAN_TYPE);
	}

	public static boolean isFloat(final @NotNull PsiType type) {
		return type.getPresentableText()
		           .equals(FLOAT_TYPE);
	}

	public static boolean isInt(final @NotNull PsiType type) {
		return type.getPresentableText()
		           .equals(INT_TYPE);
	}

	public static boolean isLong(final @NotNull PsiType type) {
		return type.getPresentableText()
		           .equals(LONG_TYPE);
	}

	public static boolean hasConstInitializer(final @NotNull UField node) {
		final UExpression initializer = node.getUastInitializer();
		return initializer != null && initializer.evaluate() != null;
	}

	public static boolean hasAnnotation(final PsiModifierListOwner field, Class<? extends Annotation> annotationClass) {
		for (PsiAnnotation psiAnnotation : field.getAnnotations()) {
			if (isOfType(psiAnnotation, annotationClass)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasOneOfAnnotations(final PsiModifierListOwner field, Class<? extends Annotation>... annotationClasses) {
		for (PsiAnnotation psiAnnotation : field.getAnnotations()) {
			if (isOneOfTypes(psiAnnotation, annotationClasses)) {
				return true;
			}
		}
		return false;
	}

	public static <T> T getAnnotationOwner(UAnnotation annotation) {
		return (T) annotation.getUastParent();
	}

	public static boolean isFieldReference(UExpression expression) {
		return expression instanceof JavaUSimpleNameReferenceExpression;
	}

	public static boolean isFieldReference(PsiElement expression) {
		return expression instanceof PsiReferenceExpression;
	}

	public static String getQualifiedName(final PsiType type) {
		final PsiClass psiClass = PsiTypesUtil.getPsiClass(type);
		return psiClass != null ? psiClass.getQualifiedName() : JAVA_LANG_PACKAGE + type.getPresentableText();
	}

	public static boolean isStatic(UMethod element) {
		return element.hasModifier(JvmModifier.STATIC);
	}

	public static String getFieldStringConst(final @NotNull UField node) {
		return (String) node.getUastInitializer()
		                    .evaluate();
	}

	static String getPackageName(final PsiField configField) {
		return PsiUtil.getPackageName(configField.getContainingClass());
	}

	public static PsiClass getAnnotationDeclarationClass(final PsiAnnotation configAnnotation) {
		final PsiJavaCodeReferenceElement nameReferenceElement = configAnnotation.getNameReferenceElement();
		return nameReferenceElement != null ? (PsiClass) nameReferenceElement.resolve() : null;
	}

	public static UClass getContainingClass(UElement element) {
		if (element instanceof UClass) {
			return (UClass) element;
		}
		if (element == null) {
			return null;
		}

		return getContainingClass(element.getUastParent());
	}

	static boolean isAnnotationOfType(PsiAnnotation node, Class<? extends Annotation> annotationClass) {
		return annotationClass.getCanonicalName()
		                      .equalsIgnoreCase(node.getQualifiedName());
	}

	public static UMethod[] getConstructors(final UClass node) {
		final List<UMethod> constructors = new ArrayList<>();
		final UMethod[] methods = node.getMethods();
		for (UMethod method : methods) {
			if (method.isConstructor()) {
				constructors.add(method);
			}
		}
		return constructors.toArray(new UMethod[0]);
	}

	public static List<UExpression> getExpressions(UMethod method) {
		return ((JavaUCodeBlockExpression) method.getUastBody()).getExpressions();
	}

	static String getInnerClassName(PsiMember field) {
		final StringBuilder builder = new StringBuilder();
		PsiMember currElement = field;
		while ((currElement = currElement.getContainingClass()) != null) {
			builder.insert(0, currElement.getName() + ".");
		}
		final String res = builder.toString();
		return res.substring(0, res.length() - 1);
	}
}
