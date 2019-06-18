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
import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.enumsProvider.EnumsProviderClassBuilder;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElementFactory;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElementFactory;
import com.ironsource.aura.aircon.compiler.providerBuilder.RemoteConfigProviderClassBuilder;
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

	private static final String ATTRIBUTE_AUX_TARGET = "value";

	private ProcessingEnvironment mProcessingEnvironment;

	@Override
	public synchronized void init(final javax.annotation.processing.ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
		mProcessingEnvironment = new ProcessingEnvironment(mProcessingUtils);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return toAnnotationSet(FeatureRemoteConfig.class, RemoteIntValue.class, RemoteStringValue.class, ConfigDefaultValueProvider.class, ConfigAdapter.class, ConfigValidator.class);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			generateEnumsProvider(roundEnv);

			final Set<? extends Element> featureRemoteConfigClassElements = roundEnv.getElementsAnnotatedWith(FeatureRemoteConfig.class);

			createConfigElements(roundEnv, featureRemoteConfigClassElements);

			generateRemoteConfigProviders(featureRemoteConfigClassElements);

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

		final Map<String, ExecutableElement> defaultValueProviders = getAuxMethods(roundEnv, ConfigDefaultValueProvider.class);
		final Map<String, ExecutableElement> adapters = getAuxMethods(roundEnv, ConfigAdapter.class);
		final Map<String, ExecutableElement> validators = getAuxMethods(roundEnv, ConfigValidator.class);
		final Map<String, ExecutableElement> mocks = getAuxMethods(roundEnv, ConfigMock.class);

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
				final ConfigGroupElement configGroupElement = ConfigGroupElementFactory.createConfigGroupElement(providerClassName, configClass, variableElement, configGroupAnnotation, mProcessingEnvironment.getConfigElements(), mProcessingEnvironment.getConfigGroupElements());
				mProcessingEnvironment.addConfigGroupElement(configClass, configGroupElement);
			}
		}
	}

	private <A extends Annotation> Map<String, ExecutableElement> getAuxMethods(final RoundEnvironment roundEnv, Class<A> annotationClass) {
		final Map<String, ExecutableElement> map = new HashMap<>();
		final Set<? extends Element> auxMethods = roundEnv.getElementsAnnotatedWith(annotationClass);
		for (Element method : auxMethods) {
			final String auxMethodTarget = getAuxMethodTarget(method, annotationClass);
			map.put(auxMethodTarget, (ExecutableElement) method);
		}
		return map;
	}

	private String getAuxMethodTarget(Element auxMethod, Class<? extends Annotation> annotationClass) {
		try {
			return (String) annotationClass.getMethod(ATTRIBUTE_AUX_TARGET)
			                               .invoke(auxMethod.getAnnotation(annotationClass));
		} catch (Exception e) {
			return null;
		}
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
