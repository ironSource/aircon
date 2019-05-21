package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.generated.GeneratedMethod;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/20/2018.
 */
public abstract class AbstractConfigProviderGenerator <T extends AbstractConfigElement>
		implements ConfigProviderGenerator {

	private static final String CONFIGURED_PREDICATE_SUFFIX = "Configured";

	protected static final String PARAM_CONFIG_SOURCE_ID = "configSourceId";

	protected final T mElement;

	protected GeneratedMethod mPredicateGeneratedMethod;

	AbstractConfigProviderGenerator(final T element) {
		mElement = element;
	}

	//region Predicate generation

	@Override
	public MethodSpec createConfiguredPredicate(final ClassName className) {
		return createConfiguredPredicateBuilder(className).build();
	}

	protected MethodSpec.Builder createConfiguredPredicateBuilder(final ClassName className) {
		final String methodName = getConfiguredPredicateMethodName();
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
		                                             .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
		                                             .returns(TypeName.BOOLEAN);

		final Set<ParameterSpec> parameterSpecs = getConfigMethodParameters();
		builder.addParameters(parameterSpecs);

		mPredicateGeneratedMethod = createGeneratedMethod(className, methodName, parameterSpecs);
		mElement.setConfiguredPredicate(mPredicateGeneratedMethod);

		return builder.addCode(getConfiguredPredicateBodyCodeBlock());
	}

	private String getConfiguredPredicateMethodName() {
		return Consts.PREDICATE_METHOD_PREFIX + NamingUtils.underScoreToCamel(mElement.getName(), true) + CONFIGURED_PREDICATE_SUFFIX;
	}

	protected abstract CodeBlock getConfiguredPredicateBodyCodeBlock();

	//endregion

	//region Getter generation
	protected MethodSpec.Builder createGetterMethodBuilder(final ClassName className, String configName, TypeName returnType) {
		final String methodName = getGetterMethodName(configName);
		final Set<ParameterSpec> parameterSpecs = getGetterParameters();
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
		                                             .addModifiers(getConfigMethodModifiers())
		                                             .returns(returnType)
		                                             .addParameters(parameterSpecs);

		mElement.setProviderMethod(createGeneratedMethod(className, methodName, parameterSpecs));
		return builder.addCode(getGetterBodyCodeBlock());
	}

	protected List<Modifier> getConfigMethodModifiers() {
		return Arrays.asList(Modifier.PUBLIC, Modifier.STATIC);
	}

	@Override
	public List<MethodSpec> createAdditionalPredicates(final ClassName className) {
		return new ArrayList<>();
	}

	protected abstract CodeBlock getGetterBodyCodeBlock();

	protected String getGetterMethodName(String configName) {
		return getGetterMethodPrefix() + NamingUtils.underScoreToCamel(configName, true);
	}

	protected String getGetterMethodPrefix() {
		return getGetterMethodPrefix(mElement.getUnboxedType());
	}

	protected static String getGetterMethodPrefix(TypeName typeName) {
		return typeName == TypeName.BOOLEAN ? Consts.PREDICATE_METHOD_PREFIX : Consts.GETTER_METHOD_PREFIX;
	}

	protected abstract Set<ParameterSpec> getGetterParameters();

	protected abstract Set<ParameterSpec> getConfigMethodParameters();

	protected ParameterSpec getConfigSourceIdentifierParamSpec(TypeMirror type) {
		return ParameterSpec.builder(TypeName.get(type), PARAM_CONFIG_SOURCE_ID)
		                    .build();
	}

	//endregion

	//region Util methods

	protected GeneratedMethod createGeneratedMethod(final ClassName className, final String methodName, final Set<ParameterSpec> parameterSpecs) {
		final List<GeneratedMethod.Parameter> parameters = new ArrayList<>();

		for (ParameterSpec parameterSpec : parameterSpecs) {
			parameters.add(new GeneratedMethod.Parameter(parameterSpec.name, parameterSpec.type));
		}

		return new GeneratedMethod(className, methodName, parameters);
	}
	//endregion
}
