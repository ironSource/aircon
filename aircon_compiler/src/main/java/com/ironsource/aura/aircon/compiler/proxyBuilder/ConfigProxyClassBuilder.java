package com.ironsource.aura.aircon.compiler.proxyBuilder;

import com.google.auto.common.Visibility;
import com.ironsource.aura.aircon.compiler.AbstractConfigClassBuilder;
import com.ironsource.aura.aircon.compiler.ProcessingEnvironment;
import com.ironsource.aura.aircon.compiler.consts.Strings;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.remote.RemoteMethodElement;
import com.ironsource.aura.aircon.compiler.model.remote.RemoteMethodElementFactory;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.ironsource.aura.aircon.compiler.utils.ProcessingUtils;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 11/6/2018.
 */
// PENDING - Broken for identifiable sources!
public class ConfigProxyClassBuilder
		extends AbstractConfigClassBuilder {

	private static final String REMOTE_PROXY_CLASS_PREFIX = "RemoteProxy";

	private static final String SUPER_CLASS_GENERIC = "Super";

	private TypeElement               mClass;
	private List<RemoteMethodElement> mRemoteMethods;

	private TypeSpec.Builder mProxyClassTypeSpec;

	public ConfigProxyClassBuilder(final ProcessingEnvironment processingEnvironment) {
		super(processingEnvironment);
	}

	public TypeSpec build(final TypeElement clazz, final List<ExecutableElement> remoteMethods) {
		mClass = clazz;

		initRemoteMethods(remoteMethods);

		mProxyClassTypeSpec = createTypeSpecBuilder();

		addRemoteMethods();

//		addFeedGetterMethods();

		return mProxyClassTypeSpec.build();
	}

	private void initRemoteMethods(final List<ExecutableElement> remoteMethods) {
		mRemoteMethods = new ArrayList<>();
		for (ExecutableElement remoteMethod : remoteMethods) {
			final RemoteMethodElement remoteMethodElement = RemoteMethodElementFactory.create(remoteMethod, mProcessingEnvironment.getConfigElements(), mProcessingEnvironment.getConfigGroupElements());
			mRemoteMethods.add(remoteMethodElement);
		}
	}

	private TypeSpec.Builder createTypeSpecBuilder() {
		final TypeSpec.Builder builder = TypeSpec.classBuilder(REMOTE_PROXY_CLASS_PREFIX + mClass.getSimpleName())
		                                         .addJavadoc(Strings.COMMENT_AUTO_GENERATED)
		                                         .addModifiers(Modifier.ABSTRACT);

		final List<? extends TypeParameterElement> typeParameters = mClass.getTypeParameters();
		for (TypeParameterElement typeParameter : typeParameters) {
			builder.addTypeVariable(TypeVariableName.get(typeParameter));
		}

		builder.addTypeVariable(TypeVariableName.get(SUPER_CLASS_GENERIC));

		final TypeMirror superClass = getSuperClass();
		if (superClass != null) {
			builder.superclass(TypeName.get(superClass));
			addSuperClassConstructors(builder, superClass);
		}
		return builder;
	}

	private void addSuperClassConstructors(final TypeSpec.Builder builder, final TypeMirror superClass) {
		final TypeElement superClassElement = (TypeElement) mProcessingEnvironment.getTypeUtils()
		                                                                          .asElement(superClass);
		final Map<TypeMirror, TypeName> genericMapping = getGenericsMapping(superClass, superClassElement);
		final List<ExecutableElement> constructors = getNonPrivateConstructors(superClassElement);
		for (ExecutableElement constructor : constructors) {
			final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();

			final Modifier visibilityModifier = ProcessingUtils.getVisibilityModifier(constructor);
			if (visibilityModifier != null) {
				constructorBuilder.addModifiers(visibilityModifier);
			}

			final CodeBlockBuilder superCallBuilder = new CodeBlockBuilder();
			final List<String> params = new ArrayList<>();

			final List<? extends VariableElement> parameters = constructor.getParameters();
			for (VariableElement parameter : parameters) {
				final String paramName = parameter.getSimpleName()
				                                  .toString();
				final TypeName type = genericMapping.get(parameter.asType());
				constructorBuilder.addParameter(type != null ? type : TypeName.get(parameter.asType()), paramName);
				params.add(paramName);
			}

			superCallBuilder.addSuperMethodCall(params);
			superCallBuilder.endStatement();

			constructorBuilder.addCode(superCallBuilder.build());

			builder.addMethod(constructorBuilder.build());
		}
	}

	private Map<TypeMirror, TypeName> getGenericsMapping(final TypeMirror superClass, final TypeElement superClassElement) {
		final HashMap<TypeMirror, TypeName> mapping = new HashMap<>();
		final List<? extends TypeParameterElement> generics = superClassElement.getTypeParameters();
		final List<? extends TypeMirror> resolvedGenerics = ((DeclaredType) superClass).getTypeArguments();
		for (int i = 0 ; i < generics.size() ; i++) {
			mapping.put(generics.get(i)
			                    .asType(), TypeName.get(resolvedGenerics.get(i)));
		}

		return mapping;
	}

	private List<ExecutableElement> getNonPrivateConstructors(final TypeElement superClassElement) {
		final List<ExecutableElement> constructors = new ArrayList<>();
		for (Element element : superClassElement.getEnclosedElements()) {
			if (element.getKind() == ElementKind.CONSTRUCTOR && Visibility.ofElement(element) != Visibility.PRIVATE) {
				constructors.add((ExecutableElement) element);
			}
		}
		return constructors;
	}

	private TypeMirror getSuperClass() {
		final List<? extends TypeMirror> typeArguments = ((DeclaredType) mClass.getSuperclass()).getTypeArguments();
		return !typeArguments.isEmpty() ? typeArguments.get(typeArguments.size() - 1) : null;
	}

	private void addRemoteMethods() {
		for (RemoteMethodElement remoteMethod : mRemoteMethods) {
			mProxyClassTypeSpec.addMethods(createRemoteMethodTypeSpecs(remoteMethod));
		}
	}

	private List<MethodSpec> createRemoteMethodTypeSpecs(final RemoteMethodElement remoteMethodElement) {
		final String methodName = remoteMethodElement.getName();
		final boolean methodHasOnlyFlag = remoteMethodElement.hasFlagConfigElement() && !remoteMethodElement.hasRemoteParameters();
		final String actionMethodName = NamingUtils.getActionMethodName(methodName, methodHasOnlyFlag);
		final MethodSpec.Builder actionMethodBuilder = MethodSpec.methodBuilder(actionMethodName)
		                                                         .addModifiers(remoteMethodElement.isApiMethod() ? Modifier.PUBLIC : Modifier.PROTECTED);

		actionMethodBuilder.returns(remoteMethodElement.hasFlagConfigElement() ? TypeName.BOOLEAN : TypeName.get(remoteMethodElement.getReturnType()));

		addApiMethodParameters(remoteMethodElement, actionMethodBuilder);

		addRemoteMethodBody(remoteMethodElement, methodName, actionMethodBuilder);

		final MethodSpec.Builder abstractActionMethodBuilder = MethodSpec.methodBuilder(methodName)
		                                                                 .addModifiers(Modifier.PROTECTED);

		abstractActionMethodBuilder.addModifiers(Modifier.ABSTRACT);
		abstractActionMethodBuilder.returns(TypeName.get(remoteMethodElement.getReturnType()));

		addRemoteMethodParameters(remoteMethodElement, abstractActionMethodBuilder);

		return Arrays.asList(actionMethodBuilder.build(), abstractActionMethodBuilder.build());
	}

	private void addRemoteMethodParameters(final RemoteMethodElement remoteMethodElement, final MethodSpec.Builder builder) {
		addApiMethodParameters(remoteMethodElement, builder);

		for (final AbstractConfigElement configElement : remoteMethodElement.getRemoteParameters()) {
			builder.addParameter(configElement.getUnboxedType(), NamingUtils.underScoreToCamel(configElement.getName(), false));
		}
	}

	private void addRemoteMethodBody(final RemoteMethodElement remoteMethodElement, final String methodName, final MethodSpec.Builder builder) {
		final CodeBlock actionPerformMethodCall = createAbstractMethodCall(remoteMethodElement, methodName);
		addRemoteMethodBody(remoteMethodElement, builder, actionPerformMethodCall);
	}

	private void addRemoteMethodBody(final RemoteMethodElement remoteMethodElement, final MethodSpec.Builder actionMethodBuilder, final CodeBlock actionPerformMethodCall) {
		if (remoteMethodElement.hasFlagConfigElement()) {
			final ConfigElement flagConfigElement = remoteMethodElement.getFlagConfigElement();
			final CodeBlockBuilder bodyBuilder = new CodeBlockBuilder().beginIf(getConfigProviderCall(flagConfigElement));
			if (remoteMethodElement.isPredicate()) {
				bodyBuilder.addReturn(actionPerformMethodCall);
			}
			else {
				bodyBuilder.add(actionPerformMethodCall)
				           .endStatement()
				           .addReturn(Boolean.TRUE.toString());
			}

			bodyBuilder.endIf()
			           .addReturn(Boolean.FALSE.toString())
			           .build();

			actionMethodBuilder.addCode(bodyBuilder.build());
		}
		else {
			final CodeBlockBuilder bodyBuilder = new CodeBlockBuilder();
			if (!remoteMethodElement.isVoid()) {
				bodyBuilder.addReturn(actionPerformMethodCall);
			}
			else {
				bodyBuilder.add(actionPerformMethodCall);
				bodyBuilder.endStatement();
			}
			actionMethodBuilder.addCode(bodyBuilder.build());
		}
	}

	private CodeBlock createConfigGetterMethodCall(final AbstractConfigElement configElement) {
		final Map<String, Object> values = new HashMap<>();
//		final Object productFeed = getFeedGetterMethodCall(ConfigSource.PRODUCT_FEED);
//		final Object appFeed = getFeedGetterMethodCall(ConfigSource.APP_FEED);
//
//		values.put(NamingUtils.PARAMETER_PRODUCT_FEED, productFeed);
//		values.put(NamingUtils.PARAMETER_APP_FEED, appFeed);
		return new CodeBlockBuilder().addGeneratedMethodCall(configElement.getProviderMethod(), values)
		                             .build();
	}

//	private CodeBlock getFeedGetterMethodCall(ConfigSource configSource) {
//		return new CodeBlockBuilder().addMethodCall(getFeedGetterMethodName(configSource))
//		                             .build();
//	}

	private CodeBlock createAbstractMethodCall(final RemoteMethodElement remoteMethodElement, final String methodName) {
		final CodeBlockBuilder codeBlock = new CodeBlockBuilder();

		final List<? extends VariableElement> parameters = remoteMethodElement.getParameters();
		final List<AbstractConfigElement> remoteParameters = remoteMethodElement.getRemoteParameters();

		final List<Object> callParameters = new ArrayList<>();

		for (final VariableElement variableElement : parameters) {
			callParameters.add(variableElement.getSimpleName()
			                                  .toString());
		}

		for (final AbstractConfigElement configElement : remoteParameters) {
			callParameters.add(getConfigProviderCall(configElement));
		}

		return codeBlock.addMethodCall(methodName, callParameters)
		                .build();
	}

	private CodeBlock getConfigProviderCall(final AbstractConfigElement configElement) {
		return new CodeBlockBuilder().add(createConfigGetterMethodCall(configElement))
		                             .build();
	}

//	private void addFeedGetterMethods() {
//		boolean addedProductFeedGetter = false;
//		boolean addedAppFeedGetter = false;
//
//		for (final RemoteMethodElement remoteMethod : mRemoteMethods) {
//			if (!addedProductFeedGetter && requiresFeed(remoteMethod, ProductFeedClassDescriptor.CLASS_NAME)) {
//				addFeedGetterMethod(ConfigSource.PRODUCT_FEED);
//				addedProductFeedGetter = true;
//			}
//			if (!addedAppFeedGetter && requiresFeed(remoteMethod, AppFeedClassDescriptor.CLASS_NAME)) {
//				addFeedGetterMethod(ConfigSource.APP_FEED);
//				addedAppFeedGetter = true;
//			}
//
//			if (addedAppFeedGetter && addedProductFeedGetter) {
//				return;
//			}
//		}
//	}

//	private boolean requiresFeed(RemoteMethodElement remoteMethod, TypeName feedClassName) {
//		for (AbstractConfigElement configElement : remoteMethod.getRemoteParameters()) {
//			if (hasFeedParameter(configElement.getProviderMethod(), feedClassName)) {
//				return true;
//			}
//		}
//
//		if (remoteMethod.hasFlagConfigElement() && hasFeedParameter(remoteMethod.getFlagConfigElement()
//		                                                                        .getProviderMethod(), feedClassName)) {
//			return true;
//		}
//
//		return false;
//	}
//
//	private boolean hasFeedParameter(GeneratedMethod providerMethod, TypeName feedClassName) {
//		for (GeneratedMethod.Parameter parameter : providerMethod.getParameters()) {
//			if (parameter.getType()
//			             .equals(feedClassName)) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	private void addFeedGetterMethod(final ConfigSource configSource) {
//		final TypeName returnType = getConfigSourceAsTypeName(configSource);
//		mProxyClassTypeSpec.addMethod(MethodSpec.methodBuilder(getFeedGetterMethodName(configSource))
//		                                        .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
//		                                        .returns(returnType)
//		                                        .build());
//	}

//	private ClassName getConfigSourceAsTypeName(final ConfigSource configSource) {
	//		return configSource == ConfigSource.PRODUCT_FEED ? ProductFeedClassDescriptor.CLASS_NAME : AppFeedClassDescriptor.CLASS_NAME;
	//	}

	//	private String getFeedGetterMethodName(final ConfigSource configSource) {
	//		return Consts.GETTER_METHOD_PREFIX + (configSource == ConfigSource.PRODUCT_FEED ? ProductFeedClassDescriptor.CLASS_NAME.simpleName() : AppFeedClassDescriptor.CLASS_NAME.simpleName());
	//	}

	private void addApiMethodParameters(final RemoteMethodElement remoteMethodElement, final MethodSpec.Builder actionMethodBuilder) {
		for (VariableElement parameter : remoteMethodElement.getParameters()) {
			actionMethodBuilder.addParameter(TypeName.get(parameter.asType()), parameter.getSimpleName()
			                                                                            .toString());
		}
	}
}
