package com.ironsource.aura.aircon.compiler;

import com.google.auto.service.AutoService;
import com.ironsource.aura.aircon.common.annotations.ConfigAdapter;
import com.ironsource.aura.aircon.common.annotations.ConfigDefaultValueProvider;
import com.ironsource.aura.aircon.common.annotations.ConfigMock;
import com.ironsource.aura.aircon.common.annotations.ConfigValidator;
import com.ironsource.aura.aircon.common.annotations.FeatureRemoteConfig;
import com.ironsource.aura.aircon.common.annotations.config.ConfigGroup;
import com.ironsource.aura.aircon.common.annotations.config.value.RemoteIntValue;
import com.ironsource.aura.aircon.common.annotations.config.value.RemoteStringValue;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteApiMethod;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteFlag;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteParam;
import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.enumsProvider.EnumsProviderClassBuilder;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElementFactory;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElementFactory;
import com.ironsource.aura.aircon.compiler.providerBuilder.RemoteConfigProviderClassBuilder;
import com.ironsource.aura.aircon.compiler.proxyBuilder.ConfigProxyClassBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.ironsource.aura.aircon.compiler.utils.SimpleProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static com.ironsource.aura.aircon.compiler.utils.Utils.toAnnotationSet;

@AutoService(Processor.class)
public class AirConProcessor
		extends SimpleProcessor {

	private ProcessingEnvironment mProcessingEnvironment;

	@Override
	public synchronized void init(final javax.annotation.processing.ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
		mProcessingEnvironment = new ProcessingEnvironment(mProcessingUtils);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return toAnnotationSet(FeatureRemoteConfig.class, RemoteFlag.class, RemoteParam.class, RemoteApiMethod.class, RemoteIntValue.class, RemoteStringValue.class, ConfigDefaultValueProvider.class, ConfigAdapter.class, ConfigValidator.class);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			generateEnumsProvider(roundEnv);

			final Set<? extends Element> featureRemoteConfigClassElements = roundEnv.getElementsAnnotatedWith(FeatureRemoteConfig.class);

			createConfigElements(roundEnv, featureRemoteConfigClassElements);

			generateRemoteConfigProviders(featureRemoteConfigClassElements);

			// PENDING - Need to rethink this feature
			//			generateConfigProxyClasses(roundEnv);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private void generateEnumsProvider(final RoundEnvironment roundEnv) {
		final Map<TypeElement, List<VariableElement>> enumClassToConsts = mapClassElements(roundEnv, RemoteIntValue.class, RemoteStringValue.class);
		if (!enumClassToConsts.isEmpty()) {
			final TypeSpec enumsProviderClass = new EnumsProviderClassBuilder(mProcessingEnvironment).build(enumClassToConsts);
			writeClassToFile(null, Consts.BASE_AIRCON_PACKAGE, enumsProviderClass);
		}
	}

	private void createConfigElements(final RoundEnvironment roundEnv, final Set<? extends Element> featureRemoteConfigClassElements) {
		for (Element configClassElement : featureRemoteConfigClassElements) {
			createConfigElements(roundEnv, (TypeElement) configClassElement);
		}

		onPostConfigElementCreate();
	}

	private void createConfigElements(final RoundEnvironment roundEnv, final TypeElement configClass) {
		final List<VariableElement> variableElements = mProcessingUtils.getEnclosedElementsByType(configClass, VariableElement.class);

		final String packageName = getPackage(configClass);
		final ClassName providerClassName = ClassName.get(packageName, NamingUtils.getProviderClassName(configClass.getSimpleName()
		                                                                                                           .toString()));

		final Map<String, ExecutableElement> defaultValueProviders = getDefaultValueProviders(roundEnv);
		final Map<String, ExecutableElement> adapters = getAdapters(roundEnv);
		final Map<String, ExecutableElement> validators = getValidators(roundEnv);
		final Map<String, ExecutableElement> mocks = getMocks(roundEnv);

		// Create config elements
		for (VariableElement variableElement : variableElements) {
			final ConfigElement configElement = ConfigElementFactory.create(providerClassName, configClass, variableElement, mProcessingUtils.getElementUtils(), mProcessingEnvironment.getTypeUtils(), defaultValueProviders, adapters, validators, mocks);
			if (configElement != null) {
				mProcessingEnvironment.addConfigElement(configClass, configElement);
			}
		}

		// Create config groups
		createFeatureConfigGroupElement(configClass, providerClassName);

		for (VariableElement variableElement : variableElements) {
			final ConfigGroup configGroupAnnotation = variableElement.getAnnotation(ConfigGroup.class);
			if (configGroupAnnotation != null) {
				final ConfigGroupElement configGroupElement = ConfigGroupElementFactory.createConfigGroupElement(providerClassName, variableElement, configGroupAnnotation, mProcessingEnvironment.getConfigElements(), mProcessingEnvironment.getConfigGroupElements());
				mProcessingEnvironment.addConfigGroupElement(configClass, configGroupElement);
			}
		}
	}

	private Map<String, ExecutableElement> getDefaultValueProviders(final RoundEnvironment roundEnv) {
		final Map<String, ExecutableElement> defaultProvidersMap = new HashMap<>();
		final Set<? extends Element> defaultValueProviders = roundEnv.getElementsAnnotatedWith(ConfigDefaultValueProvider.class);
		for (Element defaultValueProvider : defaultValueProviders) {
			final ConfigDefaultValueProvider annotation = defaultValueProvider.getAnnotation(ConfigDefaultValueProvider.class);
			defaultProvidersMap.put(annotation.value(), (ExecutableElement) defaultValueProvider);
		}
		return defaultProvidersMap;
	}

	private Map<String, ExecutableElement> getAdapters(final RoundEnvironment roundEnv) {
		final Map<String, ExecutableElement> configAdaptersMap = new HashMap<>();
		final Set<? extends Element> defaultValueProviders = roundEnv.getElementsAnnotatedWith(ConfigAdapter.class);
		for (Element defaultValueProvider : defaultValueProviders) {
			final ConfigAdapter annotation = defaultValueProvider.getAnnotation(ConfigAdapter.class);
			configAdaptersMap.put(annotation.value(), (ExecutableElement) defaultValueProvider);
		}
		return configAdaptersMap;
	}

	private Map<String, ExecutableElement> getValidators(final RoundEnvironment roundEnv) {
		final Map<String, ExecutableElement> validatorsMap = new HashMap<>();
		final Set<? extends Element> validators = roundEnv.getElementsAnnotatedWith(ConfigValidator.class);
		for (Element validator : validators) {
			final ConfigValidator annotation = validator.getAnnotation(ConfigValidator.class);
			validatorsMap.put(annotation.value(), (ExecutableElement) validator);
		}
		return validatorsMap;
	}

	private Map<String, ExecutableElement> getMocks(final RoundEnvironment roundEnv) {
		final Map<String, ExecutableElement> mocksMap = new HashMap<>();
		final Set<? extends Element> mocks = roundEnv.getElementsAnnotatedWith(ConfigMock.class);
		for (Element mock : mocks) {
			final ConfigMock annotation = mock.getAnnotation(ConfigMock.class);
			mocksMap.put(annotation.value(), (ExecutableElement) mock);
		}
		return mocksMap;
	}

	private void createFeatureConfigGroupElement(final TypeElement configClass, final ClassName providerClassName) {
		final List<ConfigElement> configElements = mProcessingEnvironment.getClassConfigElements(configClass);
		final ConfigGroupElement featureConfigGroupElement = ConfigGroupElementFactory.createFeatureConfigGroupElement(configClass, providerClassName, configElements);
		mProcessingEnvironment.addConfigGroupElement(configClass, featureConfigGroupElement);
	}

	private void onPostConfigElementCreate() {
		// Set default config element
		for (ConfigElement configElement : mProcessingEnvironment.getConfigElements()) {
			if (configElement.hasDefaultConfigValue()) {
				final ConfigElement defaultValueConfigElement = AbstractConfigElement.getConfigElementByKey(configElement.getDefaultConfigValue(), mProcessingEnvironment.getConfigElements());
				configElement.setDefaultConfigValueElement(defaultValueConfigElement);
			}
		}
	}

	private void generateRemoteConfigProviders(final Set<? extends Element> featureRemoteConfigClassElements) {
		for (Element configClassElement : featureRemoteConfigClassElements) {
			final TypeElement configClass = (TypeElement) configClassElement;
			generateRemoteConfigProviderClass(configClass);
		}
	}

	private void generateRemoteConfigProviderClass(TypeElement configClassElement) {
		final List<TypeSpec> typeSpecs = new RemoteConfigProviderClassBuilder(mProcessingEnvironment).build(configClassElement);

		final String packageName = getPackage(configClassElement);
		for (TypeSpec typeSpec : typeSpecs) {
			writeClassToFile(configClassElement, packageName, typeSpec);
		}
	}

	private void generateConfigProxyClasses(final RoundEnvironment roundEnv) {
		final Map<TypeElement, List<ExecutableElement>> classToRemoteMethods = mapClassElements(roundEnv, RemoteFlag.class, RemoteParam.class);
		for (TypeElement clazz : classToRemoteMethods.keySet()) {
			final List<ExecutableElement> remoteMethods = extractNonConstructorMethods(classToRemoteMethods.get(clazz));
			if (!remoteMethods.isEmpty()) {
				final TypeSpec configProxyClass = new ConfigProxyClassBuilder(mProcessingEnvironment).build(clazz, remoteMethods);
				writeClassToFile(clazz, getPackage(clazz), configProxyClass);
			}
		}
	}

	private List<ExecutableElement> extractNonConstructorMethods(final List<ExecutableElement> remoteMethods) {
		final List<ExecutableElement> methods = new ArrayList<>();
		for (ExecutableElement remoteMethod : remoteMethods) {
			final boolean constructor = remoteMethod.getSimpleName()
			                                        .toString()
			                                        .equals(Consts.CONSTRUCTOR_METHOD_NAME);
			if (!constructor) {
				methods.add(remoteMethod);
			}
		}
		return methods;
	}

	private <T extends Element> Map<TypeElement, List<T>> mapClassElements(final RoundEnvironment roundEnv, Class<? extends Annotation>... annotations) {
		final Map<TypeElement, List<T>> classToElements = new HashMap<>();

		for (Class<? extends Annotation> annotation : annotations) {
			final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
			for (Element element : elements) {
				while (!(element.getEnclosingElement() instanceof TypeElement)) {
					element = element.getEnclosingElement();
				}
				addToMap(classToElements, (T) element);
			}

		}

		return classToElements;
	}

	private <T extends Element> void addToMap(final Map<TypeElement, List<T>> classToElements, final T element) {
		final TypeElement classElement = (TypeElement) element.getEnclosingElement();
		List<T> classElements = classToElements.get(classElement);
		if (classElements == null) {
			classElements = new ArrayList<>();
			classToElements.put(classElement, classElements);
		}
		if (!classElements.contains(element)) {
			classElements.add(element);
		}
	}

	private String getPackage(Element element) {
		return mProcessingUtils.getElementUtils()
		                       .getPackageOf(element)
		                       .getQualifiedName()
		                       .toString();
	}
}
