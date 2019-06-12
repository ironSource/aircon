package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.consts.Strings;
import com.ironsource.aura.aircon.compiler.descriptors.AirConClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ConfigSourceClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ResourcesClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.ConfigAuxMethod;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;


/**
 * Created on 11/7/2018.
 */
abstract class DefaultConfigProviderGenerator <T extends ConfigElement>
		extends AbstractConfigProviderGenerator<T> {

	private static final String CONFIG_JAVADOC_PREFIX              = "Controls";
	private static final String CONFIG_GETTER_JAVADOC_PREFIX       = "Returns";
	private static final String CONFIG_SETTER_JAVADOC_PREFIX       = "Sets";
	private static final String CONFIG_RAW_GETTER_SUFFIX           = "RawValue";
	private static final String CONFIG_DEFAULT_VALUE_GETTER_SUFFIX = "DefaultValue";

	private static final String VAR_DEFAULT_VALUE = "defaultValue";
	private static final String VAR_VALUE         = "value";

	private static final String INTEGER_CONFIG_TYPE = "Integer";

	DefaultConfigProviderGenerator(final T element) {
		super(element);
	}

	//region Default value getter generation
	@Override
	public MethodSpec createDefaultValueGetter(final ClassName className) {
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(getDefaultValueGetterMethodName())
		                                             .addModifiers(getConfigMethodModifiers())
		                                             .returns(mElement.getRawType());

		if (hasDefaultConfigWithIdentifiableSource()) {
			builder.addParameter(getConfigSourceIdentifierParamSpec(mElement.getDefaultConfigValueElement()
			                                                                .getSourceIdentifierTypeMirror()));
		}

		final CodeBlockBuilder codeBlockBuilder = new CodeBlockBuilder().addReturn(getDefaultValueEvaluationCodeBlock());

		return builder.addCode(codeBlockBuilder.build())
		              .build();
	}

	private String getDefaultValueGetterMethodName() {
		return Consts.GETTER_METHOD_PREFIX + NamingUtils.underScoreToCamel(mElement.getName(), true) + CONFIG_DEFAULT_VALUE_GETTER_SUFFIX;
	}
	//endregion

	//region Predicate generation
	@Override
	protected CodeBlock getConfiguredPredicateBodyCodeBlock() {
		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();

		if (mElement.hasMock()) {
			bodyCodeBuilder.addReturn(true);
		}
		else {
			bodyCodeBuilder.addReturn(getConfigSourceClassDescriptor().isConfigured(getConfigTypeStr(), getKeyParam())
			                                                          .build());
		}

		return bodyCodeBuilder.build();
	}

	protected final CodeBlock[] getSourceGetterParameters() {
		final List<CodeBlock> codeBlocks = new ArrayList<>();
		codeBlocks.add(new CodeBlockBuilder().addClassQualifier(TypeName.get(mElement.getSourceTypeMirror()))
		                                     .add(CodeBlockBuilder.CLASS)
		                                     .build());

		if (mElement.hasIdentifiableSource()) {
			codeBlocks.add(CodeBlock.of(getSourceIdentifierParamName(mElement.getSourceIdentifierTypeMirror())));
		}
		return codeBlocks.toArray(new CodeBlock[0]);
	}

	//endregion

	//region Getter generation
	@Override
	public final MethodSpec createGetter(final ClassName className) {
		final MethodSpec.Builder builder = createGetterMethodBuilder(className, mElement.getName(), mElement.getUnboxedType());
		addJavaDoc(builder);

		return builder.build();
	}

	@Override
	public MethodSpec createRawGetter(final ClassName className) {
		final MethodSpec.Builder builder = createRawGetterMethodBuilder();
		addJavaDoc(builder);

		final MethodSpec methodSpec = builder.build();
		mElement.setRawGetter(createGeneratedMethod(className, methodSpec.name, new HashSet<>(methodSpec.parameters)));
		return methodSpec;
	}

	private MethodSpec.Builder createRawGetterMethodBuilder() {
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(getRawGetterMethodName())
		                                             .addModifiers(getConfigMethodModifiers())
		                                             .returns(mElement.getUnboxedRawType())
		                                             .addParameters(getGetterParameters());

		return builder.addCode(getRawGetterBodyCodeBlock());
	}

	private String getRawGetterMethodName() {
		return Consts.GETTER_METHOD_PREFIX + NamingUtils.underScoreToCamel(mElement.getName(), true) + CONFIG_RAW_GETTER_SUFFIX;
	}

	private CodeBlock getRawGetterBodyCodeBlock() {
		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();

		final CodeBlock configValue = createValueEvaluationCode();
		bodyCodeBuilder.add(configValue);
		bodyCodeBuilder.addReturn(VAR_VALUE);

		return bodyCodeBuilder.build();
	}

	private void addJavaDoc(final MethodSpec.Builder builder) {
		final String javadoc = mElement.getJavadoc();
		builder.addJavadoc(javadoc != null ? normalizeGetterJavadoc(javadoc) : Strings.NO_JAVADOC);
	}

	private String normalizeGetterJavadoc(final String javadoc) {
		return javadoc.replace(CONFIG_JAVADOC_PREFIX, CONFIG_GETTER_JAVADOC_PREFIX);
	}

	// PENDING - maybe we can reuse the raw getter here (problem because we also need the default value sometimes)
	@Override
	protected CodeBlock getGetterBodyCodeBlock() {
		final CodeBlockBuilder bodyCodeBuilder = new CodeBlockBuilder();
		final CodeBlock configValue = createValueEvaluationCode();
		bodyCodeBuilder.add(configValue);

		// Config validator
		final Object configValueValidationExpression = createConfigValueValidationExpression(VAR_VALUE);
		if (configValueValidationExpression != null) {
			bodyCodeBuilder.addAssignmentExpression(VAR_VALUE, configValueValidationExpression);
		}

		// Config adapter
		CodeBlock value = getConversionToTypeExpression(VAR_DEFAULT_VALUE, VAR_VALUE);
		if (mElement.hasAdapter()) {
			value = createConfigAuxMethodCall(mElement.getAdapter(), value);
		}

		bodyCodeBuilder.add(new CodeBlockBuilder().addReturn(value)
		                                          .build());
		return bodyCodeBuilder.build();
	}

	@Override
	protected Set<ParameterSpec> getGetterParameters() {
		return getConfigMethodParameters();
	}

	@Override
	protected Set<ParameterSpec> getConfigMethodParameters() {
		final Set<ParameterSpec> parameterSpecs = new HashSet<>();

		if (mElement.hasIdentifiableSource()) {
			parameterSpecs.add(getConfigSourceIdentifierParamSpec(mElement.getSourceIdentifierTypeMirror()));
		}

		if (hasDefaultConfigWithIdentifiableSource()) {
			parameterSpecs.add(getConfigSourceIdentifierParamSpec(mElement.getDefaultConfigValueElement()
			                                                              .getSourceIdentifierTypeMirror()));
		}

		return parameterSpecs;
	}

	private boolean hasDefaultConfigWithIdentifiableSource() {
		return mElement.hasDefaultConfigValue() && mElement.getDefaultConfigValueElement()
		                                                   .hasIdentifiableSource();
	}

	private CodeBlock createValueEvaluationCode() {
		final CodeBlockBuilder builder = new CodeBlockBuilder();
		builder.addLocalVariable(mElement.getRawType(), VAR_DEFAULT_VALUE, getDefaultValueEvaluationCodeBlock(), true);

		if (mElement.hasMock()) {
			builder.addLocalVariable(mElement.getUnboxedRawType(), VAR_VALUE, createConfigAuxMethodCall(mElement.getMock(), false, new Object[0]), false);
		}
		else {
			builder.addLocalVariable(mElement.getUnboxedRawType(), VAR_VALUE, getRawGetterExpression(), false);
		}

		return builder.build();
	}

	private CodeBlock getDefaultValueEvaluationCodeBlock() {
		if (mElement.hasDefaultConfigValue()) {
			return getConversionToRawTypeExpression(createConfigGetterMethodCall(mElement.getDefaultConfigValueElement()));
		}
		else if (mElement.hasDefaultValueProvider()) {
			return createConfigAuxMethodCall(mElement.getDefaultValueProvider());
		}
		else {
			return getDefaultValueExpression();
		}
	}

	protected CodeBlock getDefaultValueExpression() {
		Object defaultValue = mElement.getDefaultValue();

		if (mElement.hasDefaultValueResId()) {
			final ResourcesClassDescriptor resourcesClassDescriptor = AirConClassDescriptor.get()
			                                                                               .getContext()
			                                                                               .getResources();
			defaultValue = getResId(resourcesClassDescriptor, mElement.getDefaultValueResId());
		}

		return new CodeBlockBuilder().addCast(mElement.getUnboxedRawType(), CodeBlock.of("$" + (defaultValue instanceof String ? "S" : "L"), defaultValue))
		                             .build();
	}

	protected CodeBlock getResId(final ResourcesClassDescriptor resourcesClassDescriptor, int resId) {
		ClassDescriptor classDescriptor;
		final TypeName rawType = mElement.getUnboxedRawType();
		if (rawType.equals(TypeName.INT) || rawType.equals(TypeName.LONG)) {
			classDescriptor = resourcesClassDescriptor.getInteger(resId);
		}
		else if (rawType.equals(TypeName.FLOAT)) {
			classDescriptor = resourcesClassDescriptor.getFraction(resId);
		}
		else if (rawType.equals(TypeName.BOOLEAN)) {
			classDescriptor = resourcesClassDescriptor.getBoolean(resId);
		}
		else {
			classDescriptor = resourcesClassDescriptor.getString(resId);
		}

		return classDescriptor.build();
	}

	private Object createConfigValueValidationExpression(final Object value) {
		final List<CodeBlock> validationConditions = new ArrayList<>();

		final CodeBlock validationCondition = getValidationCondition(VAR_VALUE);
		if (validationCondition != null) {
			validationConditions.add(validationCondition);
		}

		if (mElement.hasValidator()) {
			validationConditions.add(createConfigAuxMethodCall(mElement.getValidator(), value));
		}

		if (validationConditions.isEmpty()) {
			return null;
		}

		final CodeBlock validationConditionCodeBlock = new CodeBlockBuilder().addAnd(validationConditions.toArray())
		                                                                     .build();

		return new CodeBlockBuilder().addConditionalExpression(validationConditionCodeBlock, value, VAR_DEFAULT_VALUE)
		                             .build();
	}

	private CodeBlock createConfigAuxMethodCall(ConfigAuxMethod configAuxMethod, Object... params) {
		return createConfigAuxMethodCall(configAuxMethod, true, params);
	}

	private CodeBlock createConfigAuxMethodCall(ConfigAuxMethod configAuxMethod, boolean addSourceIdParam, Object... params) {
		final ArrayList<Object> paramList = new ArrayList<>(Arrays.asList(params));
		if (addSourceIdParam && mElement.hasIdentifiableSource()) {
			paramList.add(0, getSourceIdentifierParamName(mElement.getSourceIdentifierTypeMirror()));
		}
		return new CodeBlockBuilder().addStaticMethodCall(configAuxMethod.getContainingClass(), configAuxMethod.getName(), paramList.toArray())
		                             .build();
	}

	protected CodeBlock getValidationCondition(final String varValue) {
		return null;
	}

	private CodeBlock getRawGetterExpression() {
		return getConfigSourceClassDescriptor().get(getConfigTypeStr(), getKeyParam(), VAR_DEFAULT_VALUE)
		                                       .build();
	}

	protected final ConfigSourceClassDescriptor getConfigSourceClassDescriptor() {
		return AirConClassDescriptor.get()
		                            .getConfigSourceRepository()
		                            .getSource(getSourceGetterParameters());
	}

	protected String getConfigTypeStr() {
		final TypeName configType = mElement.getUnboxedRawType();
		return configType.isPrimitive() ? getConfigTypeStr(configType) : String.class.getSimpleName();
	}

	private String getConfigTypeStr(final TypeName configType) {
		return configType.equals(TypeName.INT) ? INTEGER_CONFIG_TYPE : NamingUtils.capitalizeFirstLetter(configType.toString());
	}

	private CodeBlock createConfigGetterMethodCall(final ConfigElement defaultConfigValueElement) {
		return new CodeBlockBuilder().addGeneratedMethodCall(defaultConfigValueElement.getProviderMethod())
		                             .build();
	}

	protected CodeBlock getConversionToTypeExpression(final Object varDefaultValue, final Object varValue) {
		return CodeBlockBuilder.of(varValue)
		                       .build();
	}

	//endregion

	//region Setter generation
	@Override
	public final MethodSpec createSetter(final ClassName className) {
		if (!mElement.isMutable()) {
			return null;
		}

		final String javadoc = mElement.getJavadoc();
		final MethodSpec.Builder builder = MethodSpec.methodBuilder(getConfigSetterMethodName())
		                                             .addModifiers(getConfigMethodModifiers())
		                                             .addJavadoc(javadoc != null ? normalizeSetterJavadoc(javadoc) : Strings.NO_JAVADOC);

		builder.addParameters(getConfigMethodParameters());
		builder.addParameter(mElement.getUnboxedDeclaredType(), VAR_VALUE);

		addSetterSetStatement(builder);
		return builder.build();
	}

	@Override
	protected List<Modifier> getConfigMethodModifiers() {
		if (mElement.isMutable() || (mElement.hasDefaultConfigValue() && mElement.getDefaultConfigValueElement()
		                                                                         .isMutable())) {
			final List<Modifier> modifiers = new ArrayList<>(super.getConfigMethodModifiers());
			modifiers.add(Modifier.SYNCHRONIZED);
			return modifiers;
		}
		return super.getConfigMethodModifiers();
	}

	private void addSetterSetStatement(final MethodSpec.Builder builder) {
		final CodeBlockBuilder codeBlockBuilder = new CodeBlockBuilder();
		codeBlockBuilder.add(getConfigSourceClassDescriptor().put(getConfigTypeStr(), getKeyParam(), getConversionToRawTypeExpression(VAR_VALUE))
		                                                     .build());
		codeBlockBuilder.endStatement();
		builder.addCode(codeBlockBuilder.build());
	}

	protected CodeBlock getConversionToRawTypeExpression(final Object value) {
		return CodeBlockBuilder.of(value)
		                       .build();
	}

	private String getConfigSetterMethodName() {
		return Consts.SETTER_METHOD_PREFIX + NamingUtils.underScoreToCamel(mElement.getName(), true);
	}

	private String normalizeSetterJavadoc(final String javadoc) {
		return javadoc.replace(CONFIG_JAVADOC_PREFIX, CONFIG_SETTER_JAVADOC_PREFIX);
	}
	//endregion

	//region Default value getter generation
	@Override
	public List<TypeSpec> createAdditionalClasses() {
		return new ArrayList<>();
	}
	//endregion

	//region Util methods

	protected String getKeyParam() {
		return String.format("\"%s\"", mElement.getKey());
	}

	//endregion
}
