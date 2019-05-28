package com.ironsource.aura.aircon.compiler.enumsProvider;

import com.ironsource.aura.aircon.common.annotations.config.value.RemoteIntValue;
import com.ironsource.aura.aircon.common.annotations.config.value.RemoteStringValue;
import com.ironsource.aura.aircon.compiler.AbstractConfigClassBuilder;
import com.ironsource.aura.aircon.compiler.ProcessingEnvironment;
import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.consts.Strings;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created on 11/24/2018.
 */
public class EnumsProviderClassBuilder
		extends AbstractConfigClassBuilder {

	private static final String PARAMETER_VALUE         = "value";
	private static final String PARAMETER_DEFAULT_VALUE = "defaultValue";

	public EnumsProviderClassBuilder(final ProcessingEnvironment processingEnvironment) {super(processingEnvironment);}

	public TypeSpec build(final Map<TypeElement, List<VariableElement>> enumClassToConsts) {
		final TypeSpec.Builder builder = TypeSpec.classBuilder(NamingUtils.ENUMS_PROVIDER_CLASS_NAME)
		                                         .addJavadoc(Strings.COMMENT_AUTO_GENERATED)
		                                         .addModifiers(Modifier.PUBLIC);
		for (TypeElement enumClass : enumClassToConsts.keySet()) {
			final List<VariableElement> consts = enumClassToConsts.get(enumClass);
			builder.addMethod(createEnumProviderMethod(enumClass, consts));
			builder.addMethod(createRemoteValueGetter(enumClass, consts));
		}

		return builder.build();
	}

	private MethodSpec createRemoteValueGetter(final TypeElement enumClass, final List<VariableElement> consts) {
		final String methodName = NamingUtils.ENUMS_PROVIDER_REMOTE_VALUE_GETTER_METHOD;
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
		                                             .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
		                                             .returns(getRemoteValueType(consts));

		builder.addParameter(TypeName.get(enumClass.asType()), PARAMETER_VALUE, Modifier.FINAL);

		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();
		bodyCodeBuilder.beginSwitch(PARAMETER_VALUE);

		for (VariableElement variableElement : consts) {
			Object remoteValue = getRemoteValue(variableElement);
			if (consts.indexOf(variableElement) == consts.size() - 1) {
				bodyCodeBuilder.addDefaultSwitchCase(null);
			}
			bodyCodeBuilder.addSwitchCase(variableElement.getSimpleName(), new CodeBlockBuilder().addReturn(remoteValue)
			                                                                                     .build());
		}

		bodyCodeBuilder.endSwitch();

		return builder.addCode(bodyCodeBuilder.build())
		              .build();
	}

	private MethodSpec createEnumProviderMethod(final TypeElement enumClass, final List<VariableElement> consts) {
		final String methodName = Consts.GETTER_METHOD_PREFIX + enumClass.getSimpleName();
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
		                                             .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
		                                             .returns(TypeName.get(enumClass.asType()));

		final TypeName parameterType = getRemoteValueType(consts);
		builder.addParameter(parameterType, PARAMETER_VALUE, Modifier.FINAL);
		builder.addParameter(parameterType, PARAMETER_DEFAULT_VALUE, Modifier.FINAL);

		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();
		final boolean checkNull = !parameterType.isPrimitive();
		if (checkNull) {
			bodyCodeBuilder.beginIf(new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_UNEQUALITY, PARAMETER_VALUE, null)
			                                              .build());
		}
		bodyCodeBuilder.beginSwitch(PARAMETER_VALUE);

		for (VariableElement variableElement : consts) {
			final CodeBlock enumConst = new CodeBlockBuilder().addClassQualifier(TypeName.get(enumClass.asType()))
			                                                  .add(variableElement.getSimpleName())
			                                                  .build();
			bodyCodeBuilder.addSwitchCase(getRemoteValue(variableElement), new CodeBlockBuilder().addReturn(enumConst)
			                                                                                     .build());
		}

		bodyCodeBuilder.endSwitch();

		if (checkNull) {
			bodyCodeBuilder.endIf();
		}

		final CodeBlock condition = new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_UNEQUALITY, PARAMETER_VALUE, PARAMETER_DEFAULT_VALUE)
		                                                  .build();
		final CodeBlock recursiveCall = new CodeBlockBuilder().addMethodCall(methodName, PARAMETER_DEFAULT_VALUE, PARAMETER_DEFAULT_VALUE)
		                                                      .build();
		bodyCodeBuilder.addReturn(new CodeBlockBuilder().addConditionalExpression(condition, recursiveCall, null)
		                                                .build());
		return builder.addCode(bodyCodeBuilder.build())
		              .build();
	}

	private TypeName getRemoteValueType(final List<VariableElement> elements) {
		return getRemoteValue(elements.iterator()
		                              .next()) instanceof String ? TypeName.get(String.class) : TypeName.INT;
	}

	private Object getRemoteValue(final VariableElement element) {
		Object remoteValue;
		final Annotation annotation = element.getAnnotation(RemoteIntValue.class);
		if (annotation != null) {
			remoteValue = ((RemoteIntValue) annotation).value();
		}
		else {
			remoteValue = element.getAnnotation(RemoteStringValue.class)
			                     .value();
		}

		if (remoteValue instanceof String) {
			remoteValue = String.format("\"%s\"", remoteValue);
		}
		return remoteValue;
	}
}
