package com.ironsource.aura.aircon.lint.utils;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiConstantEvaluationHelper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.impl.source.tree.java.PsiClassObjectAccessExpressionImpl;
import com.ironsource.aura.aircon.common.ConfigType;
import com.ironsource.aura.aircon.common.annotations.ConfigAdapter;
import com.ironsource.aura.aircon.common.annotations.ConfigDefaultValueProvider;
import com.ironsource.aura.aircon.common.annotations.ConfigMock;
import com.ironsource.aura.aircon.common.annotations.ConfigValidator;
import com.ironsource.aura.aircon.common.annotations.DefaultConfig;
import com.ironsource.aura.aircon.common.annotations.DefaultRes;
import com.ironsource.aura.aircon.common.annotations.FeatureRemoteConfig;
import com.ironsource.aura.aircon.common.annotations.Mutable;
import com.ironsource.aura.aircon.common.annotations.Source;
import com.ironsource.aura.aircon.common.annotations.config.BooleanConfig;
import com.ironsource.aura.aircon.common.annotations.config.ColorConfig;
import com.ironsource.aura.aircon.common.annotations.config.ConfigGroup;
import com.ironsource.aura.aircon.common.annotations.config.Configs;
import com.ironsource.aura.aircon.common.annotations.config.FloatConfig;
import com.ironsource.aura.aircon.common.annotations.config.IntEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.JsonConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.TextConfig;
import com.ironsource.aura.aircon.common.annotations.config.value.RemoteIntValue;
import com.ironsource.aura.aircon.common.annotations.config.value.RemoteStringValue;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteFlag;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteParam;
import com.ironsource.aura.aircon.common.utils.CommonNamingUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UAnnotated;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.java.JavaUSimpleNameReferenceExpression;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 11/8/2018.
 */
public class ConfigElementsUtils {

	public static final String ATTRIBUTE_VALUE = "value";

	private static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValue";
	private static final String ATTRIBUTE_TYPE          = "type";
	private static final String ATTRIBUTE_ENUM_CLASS    = "enumClass";
	private static final String ATTRIBUTE_JSON_TYPE     = "type";
	private static final String ATTRIBUTE_GENERIC_TYPES = "genericTypes";

	public static boolean isConfigAttributeAnnotation(UAnnotation annotation) {
		return isDefaultResAnnotation(annotation) || isDefaultConfigAnnotation(annotation) || ElementUtils.isOfType(annotation.getJavaPsi(), Mutable.class);
	}

	public static boolean hasConfigAnnotation(PsiField psiField) {
		return extractConfigAnnotation(psiField) != null;
	}

	public static PsiAnnotation extractConfigAnnotation(PsiField field) {
		for (PsiAnnotation psiAnnotation : field.getAnnotations()) {
			if (isConfigAnnotation(psiAnnotation)) {
				return psiAnnotation;
			}
		}
		return null;
	}

	public static boolean isConfigAnnotation(UAnnotation annotation) {
		return isConfigAnnotation(annotation.getJavaPsi());
	}

	private static boolean isConfigAnnotation(PsiAnnotation annotation) {
		for (Class<? extends Annotation> configClass : Configs.ALL) {
			if (ElementUtils.isOfType(annotation, configClass)) {
				return true;
			}
		}

		return isGroupAnnotation(annotation) || isCustomConfigAnnotation(annotation);
	}

	private static boolean isCustomConfigAnnotation(final PsiAnnotation annotation) {
		final PsiClass annotationClass = ElementUtils.getAnnotationDeclarationClass(annotation);
		for (final PsiAnnotation annotationClassAnnotation : annotationClass.getAnnotations()) {
			if (ElementUtils.isOfType(annotationClassAnnotation, ConfigType.class)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isGroupAnnotation(final PsiAnnotation annotation) {
		return ElementUtils.isOfType(annotation, ConfigGroup.class);
	}

	public static boolean isTextConfigAnnotation(PsiAnnotation annotation) {
		return ElementUtils.isOfType(annotation, TextConfig.class);
	}

	public static boolean isConfigField(final @NotNull PsiField node) {
		return getConfigAnnotationsCount(node) >= 1;
	}

	public static int getConfigAnnotationsCount(final @NotNull PsiField node) {
		int count = 0;
		for (PsiAnnotation annotation : node.getAnnotations()) {
			if (isConfigAnnotation(annotation)) {
				count++;
			}
		}
		return count;
	}

	public static PsiAnnotation getConfigAnnotation(final @NotNull PsiField node) {
		for (PsiAnnotation annotation : node.getAnnotations()) {
			if (isConfigAnnotation(annotation)) {
				return annotation;
			}
		}
		return null;
	}

	public static boolean isConfigGroupField(final @NotNull PsiField node) {
		for (PsiAnnotation annotation : node.getAnnotations()) {
			if (isConfigGroupAnnotation(annotation)) {
				return true;
			}
		}
		return false;
	}

	public static UAnnotation getFeatureRemoteConfigAnnotation(final UClass node) {
		return node.findAnnotation(FeatureRemoteConfig.class.getCanonicalName());
	}

	public static PsiAnnotation getFeatureRemoteConfigAnnotation(final PsiClass node) {
		for (PsiAnnotation psiAnnotation : node.getAnnotations()) {
			if (ElementUtils.isOfType(psiAnnotation, FeatureRemoteConfig.class)) {
				return psiAnnotation;
			}
		}
		return null;
	}

	public static boolean hasFeatureRemoteConfigAnnotation(final UClass node) {
		return getFeatureRemoteConfigAnnotation(node) != null;
	}

	public static boolean hasFeatureRemoteConfigAnnotation(final PsiClass node) {
		return getFeatureRemoteConfigAnnotation(node) != null;
	}

	public static UAnnotation getConfigGroupAnnotation(final UField node) {
		return node.findAnnotation(ConfigGroup.class.getCanonicalName());
	}

	public static PsiArrayInitializerMemberValue getConfigGroupValuesAttribute(final PsiAnnotation configGroupAnnotation) {
		return (PsiArrayInitializerMemberValue) configGroupAnnotation.findAttributeValue(null);
	}

	public static Object getDefaultValueAttribute(final PsiAnnotation configAnnotation) {
		return getAttributeValue(configAnnotation, ATTRIBUTE_DEFAULT_VALUE);
	}

	public static boolean hasDefaultValueAttribute(final PsiField field) {
		return getConfigAnnotation(field).findDeclaredAttributeValue(ATTRIBUTE_DEFAULT_VALUE) != null;
	}

	public static boolean hasDefaultConfigAnnotation(final PsiField field) {
		return ElementUtils.hasAnnotation(field, DefaultConfig.class);
	}

	public static boolean hasDefaultResAnnotation(final PsiField field) {
		return ElementUtils.hasAnnotation(field, DefaultRes.class);
	}

	public static boolean hasSourceAnnotation(final PsiModifierListOwner field) {
		return ElementUtils.hasAnnotation(field, Source.class);
	}

	public static <T> T getAttributeValue(final PsiAnnotation configAnnotation, String attribute) {
		final PsiAnnotationMemberValue attributeValue = configAnnotation.findAttributeValue(attribute);

		if (attributeValue == null) {
			return null;
		}

		final PsiConstantEvaluationHelper evaluationHelper = JavaPsiFacade.getInstance(attributeValue.getProject())
		                                                                  .getConstantEvaluationHelper();
		return (T) evaluationHelper.computeConstantExpression(attributeValue);
	}

	public static PsiAnnotation getDefaultConfigAnnotation(final PsiField field) {
		for (PsiAnnotation psiAnnotation : field.getAnnotations()) {
			if (ElementUtils.isOfType(psiAnnotation, DefaultConfig.class)) {
				return psiAnnotation;
			}
		}
		return null;
	}

	public static boolean isEnumConfigAnnotation(final @NotNull PsiAnnotation node) {
		for (Class<? extends Annotation> configAnnotation : Configs.ENUM) {
			if (ElementUtils.isAnnotationOfType(node, configAnnotation)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isColorConfigAnnotation(final PsiAnnotation node) {
		return ElementUtils.isAnnotationOfType(node, ColorConfig.class);
	}

	public static boolean isBooleanConfigAnnotation(final PsiAnnotation node) {
		return ElementUtils.isAnnotationOfType(node, BooleanConfig.class);
	}

	public static boolean isFloatConfigAnnotation(final PsiAnnotation node) {
		return ElementUtils.isAnnotationOfType(node, FloatConfig.class);
	}

	public static boolean isRemoteFlagAnnotation(final UAnnotation node) {
		return ElementUtils.isAnnotationOfType(node.getJavaPsi(), RemoteFlag.class);
	}

	public static boolean isRemoteParamAnnotation(final UAnnotation node) {
		return ElementUtils.isAnnotationOfType(node.getJavaPsi(), RemoteParam.class);
	}

	public static boolean isJsonConfigAnnotation(final PsiAnnotation node) {
		return ElementUtils.isAnnotationOfType(node, JsonConfig.class);
	}

	public static boolean isConfigGroupAnnotation(final UAnnotation node) {
		return isConfigGroupAnnotation(node.getJavaPsi());
	}

	public static boolean isConfigGroupAnnotation(final PsiAnnotation node) {
		return ElementUtils.isAnnotationOfType(node, ConfigGroup.class);
	}

	public static boolean isEnumAnnotation(final UAnnotation node) {
		return ElementUtils.isAnnotationOfType(node.getJavaPsi(), StringEnumConfig.class) || ElementUtils.isAnnotationOfType(node.getJavaPsi(), IntEnumConfig.class);
	}

	public static boolean isDefaultConfigAnnotation(final UAnnotation node) {
		return ElementUtils.isAnnotationOfType(node.getJavaPsi(), DefaultConfig.class);
	}

	public static boolean isDefaultResAnnotation(final UAnnotation node) {
		return ElementUtils.isAnnotationOfType(node.getJavaPsi(), DefaultRes.class);
	}

	public static UExpression getRemoteAnnotationConfigValue(UAnnotation annotation) {
		return annotation.findAttributeValue(ATTRIBUTE_VALUE);
	}

	public static String getRemoteAnnotationReferencedConfigAnnotationType(final UAnnotation annotation) {
		return getConfigFieldType(getReferencedConfigField(annotation, ATTRIBUTE_VALUE));
	}

	public static String getConfigFieldType(final PsiField configField) {
		final PsiAnnotation configAnnotation = extractConfigAnnotation(configField);

		if (configAnnotation == null) {
			return null;
		}

		if (isJsonConfigAnnotation(configAnnotation)) {
			final PsiImmediateClassType jsonType = getAttributeValue(configAnnotation, ATTRIBUTE_TYPE);
			return jsonType.resolve()
			               .getQualifiedName();
		}
		else if (isEnumConfigAnnotation(configAnnotation)) {
			return getEnumConfigImplClass(configAnnotation);
		}
		else if (isColorConfigAnnotation(configAnnotation)) {
			return getColorIntClass();
		}
		else if (isConfigGroupAnnotation(configAnnotation)) {
			return getConfigGroupImplClass(configField);
		}
		else {
			final PsiClass annotationClass = ElementUtils.getAnnotationDeclarationClass(configAnnotation);
			return ElementUtils.getQualifiedName(getDefaultValueMethod(annotationClass).getReturnType());
		}
	}

	private static String getEnumConfigImplClass(final PsiAnnotation annotation) {
		final PsiClass enumClass = getEnumClassAttribute(annotation);
		return enumClass.getQualifiedName();
	}

	public static boolean hasRemoteValueAnnotation(PsiField field) {
		return getRemoteValueAnnotation(field) != null;
	}

	public static PsiAnnotation getRemoteValueAnnotation(PsiField field) {
		for (final PsiAnnotation psiAnnotation : field.getAnnotations()) {
			if (isRemoteValueAnnotation(psiAnnotation)) {
				return psiAnnotation;
			}
		}
		return null;
	}

	public static boolean isRemoteValueAnnotation(final PsiAnnotation psiAnnotation) {
		return ElementUtils.isAnnotationOfType(psiAnnotation, RemoteIntValue.class) || ElementUtils.isAnnotationOfType(psiAnnotation, RemoteStringValue.class);
	}

	public static Object getRemoteValue(PsiField field) {
		for (final PsiAnnotation psiAnnotation : field.getAnnotations()) {
			if (isRemoteValueAnnotation(psiAnnotation)) {
				return getAttributeValue(psiAnnotation, null);
			}
		}
		return null;
	}

	public static PsiClass getEnumClassAttribute(final PsiAnnotation annotation) {
		return getClassAttribute(annotation, ATTRIBUTE_ENUM_CLASS);
	}

	public static PsiClass getJsonTypeAttribute(final PsiAnnotation annotation) {
		return getClassAttribute(annotation, ATTRIBUTE_JSON_TYPE);
	}

	@SuppressWarnings("unchecked")
	private static PsiClass getClassAttribute(final PsiAnnotation annotation, String attribute) {
		final PsiClassObjectAccessExpressionImpl attributeValue = (PsiClassObjectAccessExpressionImpl) annotation.findAttributeValue(attribute);
		return resolveClass(attributeValue);
	}

	public static PsiClass[] getGenericTypesAttribute(final PsiAnnotation annotation) {
		final PsiArrayInitializerMemberValue attributeValue = (PsiArrayInitializerMemberValue) annotation.findAttributeValue(ATTRIBUTE_GENERIC_TYPES);

		List<PsiClass> psiClasses = new ArrayList<>();

		final PsiAnnotationMemberValue[] initializerList = attributeValue.getInitializers();
		for (PsiAnnotationMemberValue initializer : initializerList) {
			psiClasses.add(resolveClass((PsiClassObjectAccessExpressionImpl) initializer));
		}

		return psiClasses.toArray(new PsiClass[0]);
	}

	private static PsiClass resolveClass(final PsiClassObjectAccessExpressionImpl expression) {
		return (PsiClass) expression.getOperand()
		                            .getInnermostComponentReferenceElement()
		                            .resolve();
	}

	private static String getConfigGroupImplClass(final PsiField configField) {
		final String configValuesEnumName = CommonNamingUtils.getConfigGroupClassName((String) configField.computeConstantValue());
		return ElementUtils.getPackageName(configField) + "." + configValuesEnumName;
	}

	private static String getColorIntClass() {
		return "com.ironsource.aura.aircon.utils.ColorInt";
	}

	private static PsiMethod getDefaultValueMethod(final PsiClass annotationClass) {
		return annotationClass.findMethodsByName(ATTRIBUTE_DEFAULT_VALUE, false)[0];
	}

	private static PsiField getReferencedConfigField(final UAnnotation annotation, String attribute) {
		final UExpression value = annotation.findAttributeValue(attribute);
		return (PsiField) ((JavaUSimpleNameReferenceExpression) value).resolve();
	}

	public static boolean isRemoteMethod(final PsiMethod method) {
		if (hasRemoteFlagAnnotation(method)) {
			return true;
		}

		for (final PsiParameter psiParameter : method.getParameterList()
		                                             .getParameters()) {
			if (isRemoteParameter(psiParameter)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasRemoteFlagAnnotation(final PsiMethod method) {
		for (final PsiAnnotation psiAnnotation : method.getAnnotations()) {
			if (ElementUtils.isAnnotationOfType(psiAnnotation, RemoteFlag.class)) {
				return true;
			}
		}
		return false;
	}

	public static List<PsiParameter> getNonRemoteParameters(PsiMethod method) {
		final List<PsiParameter> res = new ArrayList<>();
		for (PsiParameter parameter : method.getParameterList()
		                                    .getParameters()) {
			if (!isRemoteParameter(parameter)) {
				res.add(parameter);
			}
		}
		return res;
	}

	public static boolean isRemoteParameter(final PsiParameter parameter) {
		for (PsiAnnotation jvmAnnotation : parameter.getAnnotations()) {
			if (ElementUtils.isAnnotationOfType(jvmAnnotation, RemoteParam.class)) {
				return true;
			}
		}
		return false;
	}

	public static UAnnotation getDefaultValueProviderAnnotation(UMethod method) {
		for (UAnnotation annotation : ((UAnnotated) method).getAnnotations()) {
			if (ElementUtils.isOfType(annotation.getJavaPsi(), ConfigDefaultValueProvider.class)) {
				return annotation;
			}
		}
		return null;
	}

	public static boolean isConfigAuxMethod(final UMethod node) {
		return ElementUtils.hasOneOfAnnotations(node, ConfigDefaultValueProvider.class, ConfigAdapter.class, ConfigValidator.class, ConfigMock.class);
	}

	public static boolean isConfigAuxAnnotation(final UAnnotation node) {
		return ElementUtils.isOneOfTypes(node.getJavaPsi(), ConfigDefaultValueProvider.class, ConfigAdapter.class, ConfigValidator.class, ConfigMock.class);
	}

	public static boolean isConfigFieldReference(final PsiElement element) {
		if (!ElementUtils.isFieldReference(element)) {
			return false;
		}

		final PsiField configField = ElementUtils.getReferencedField(element);
		if (!isConfigField(configField)) {
			return false;
		}
		return true;
	}
}
