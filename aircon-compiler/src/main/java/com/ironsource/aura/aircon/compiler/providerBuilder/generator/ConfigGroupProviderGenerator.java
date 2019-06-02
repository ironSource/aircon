package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/14/2018.
 */
public class ConfigGroupProviderGenerator
		extends AbstractConfigProviderGenerator<ConfigGroupElement> {

	private static final String FEATURE_GROUP_GETTER = "All";

	public ConfigGroupProviderGenerator(final ConfigGroupElement element) {
		super(element);
	}

	@Override
	public MethodSpec createDefaultValueGetter(final ClassName className) {
		return null; // Not supported
	}

	//region Predicate generation
	@Override
	protected CodeBlock getConfiguredPredicateBodyCodeBlock() {
		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();

		final CodeBlockBuilder conditionBuilder = new CodeBlockBuilder();
		List<CodeBlock> conditions = new ArrayList<>();
		for (AbstractConfigElement abstractConfigElement : mElement.getConfigElements()) {
			conditions.add(createConfigConfiguredPredicateCall(abstractConfigElement));
		}
		conditionBuilder.addAnd(conditions.toArray());

		bodyCodeBuilder.addReturn(conditionBuilder.build());
		return bodyCodeBuilder.build();
	}

	//endregion

	//region Getter generation
	@Override
	public MethodSpec createGetter(final ClassName className) {
		final ClassName groupClassName = getGroupClassName();
		return createGetterMethodBuilder(className, groupClassName.simpleName(), groupClassName).build();
	}

	@Override
	public MethodSpec createRawGetter(final ClassName className) {
		return null; // Not supported
	}

	@Override
	protected String getGetterMethodName(final String configName) {
		return getGetterMethodPrefix() + (mElement.isFeatureGroup() ? FEATURE_GROUP_GETTER : configName);
	}

	@Override
	protected Set<ParameterSpec> getGetterParameters() {
		return getConfigMethodParameters();
	}

	@Override
	protected Set<ParameterSpec> getConfigMethodParameters() {
		final ArrayList<ParameterSpec> parameterSpecs = new ArrayList<>();
		getSourceIdentifierParamSpecs(mElement, parameterSpecs);
		return new HashSet<>(parameterSpecs);
	}

	private void getSourceIdentifierParamSpecs(ConfigGroupElement configGroupElement, List<ParameterSpec> parameterSpecs) {
		for (AbstractConfigElement configElement : configGroupElement.getConfigElements()) {
			if (configElement instanceof ConfigElement && ((ConfigElement) configElement).hasIdentifiableSource()) {
				final TypeMirror typeMirror = ((ConfigElement) configElement).getSourceIdentifierTypeMirror();
				parameterSpecs.add(getConfigSourceIdentifierParamSpec(typeMirror));
			}
			else if (configElement instanceof ConfigGroupElement) {
				getSourceIdentifierParamSpecs((ConfigGroupElement) configElement, parameterSpecs);
			}
		}
	}

	@Override
	protected CodeBlock getGetterBodyCodeBlock() {
		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();
		addSimpleGroupCreationCode(bodyCodeBuilder);
		return bodyCodeBuilder.build();
	}

	private void addSimpleGroupCreationCode(final CodeBlockBuilder codeBlockBuilder) {
		final List<Object> params = new ArrayList<>();
		for (final AbstractConfigElement configElement : mElement.getConfigElements()) {
			params.add(createConfigGetterMethodCall(configElement));
		}
		codeBlockBuilder.addReturn(getGroupCreationCode(params));
	}

	private CodeBlock getGroupCreationCode(List<?> params) {
		return new CodeBlockBuilder().addConstructorCall(getGroupClassName(), params)
		                             .build();
	}

	private CodeBlock createConfigGetterMethodCall(final AbstractConfigElement element) {
		return new CodeBlockBuilder().addGeneratedMethodCall(element.getProviderMethod())
		                             .build();
	}

	private CodeBlock createConfigConfiguredPredicateCall(final AbstractConfigElement element) {
		return new CodeBlockBuilder().addGeneratedMethodCall(element.getConfiguredPredicate())
		                             .build();
	}

	//endregion

	//region Group class generation
	@Override
	public List<TypeSpec> createAdditionalClasses() {
		final List<AbstractConfigElement> configElements = mElement.getConfigElements();

		final TypeSpec.Builder builder = TypeSpec.classBuilder(getGroupClassName())
		                                         .addModifiers(Modifier.PUBLIC);
		builder.addSuperinterface(TypeName.get(Serializable.class));
		for (AbstractConfigElement configElement : configElements) {
			builder.addField(configElement.getUnboxedType(), getFieldName(configElement), Modifier.PRIVATE, Modifier.FINAL);
		}

		final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
		                                                        .addModifiers(Modifier.PUBLIC);
		for (AbstractConfigElement configElement : configElements) {
			final String param = getConfigName(configElement);
			constructorBuilder.addParameter(configElement.getUnboxedType(), param, Modifier.FINAL);
			constructorBuilder.addCode(new CodeBlockBuilder().addAssignmentExpression(getFieldName(configElement), param)
			                                                 .build());
		}

		builder.addMethod(constructorBuilder.build());

		for (AbstractConfigElement configElement : configElements) {
			builder.addMethod(MethodSpec.methodBuilder(getGetterMethodPrefix(configElement.getUnboxedType()) + NamingUtils.capitalizeFirstLetter(getConfigName(configElement)))
			                            .addModifiers(Modifier.PUBLIC)
			                            .returns(configElement.getUnboxedType())
			                            .addCode(new CodeBlockBuilder().addReturn(getFieldName(configElement))
			                                                           .build())
			                            .build());
		}
		return Collections.singletonList(builder.build());
	}
	//endregion

	@Override
	public MethodSpec createSetter(final ClassName className) {
		return null; // Unsupported
	}

	//region Util methods
	private ClassName getGroupClassName() {
		return ClassName.get("", NamingUtils.getConfigGroupClassName(mElement.getKey()));
	}

	private String getFieldName(final AbstractConfigElement configElement) {
		return Consts.FIELD_PREFIX + NamingUtils.capitalizeFirstLetter(getConfigName(configElement));
	}

	private String getConfigName(final AbstractConfigElement configElement) {
		return NamingUtils.underScoreToCamel(configElement.getName(), false);
	}
	//endregion
}
