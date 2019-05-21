package com.ironsource.aura.aircon.compiler.providerBuilder;

import com.ironsource.aura.aircon.compiler.AbstractConfigClassBuilder;
import com.ironsource.aura.aircon.compiler.ProcessingEnvironment;
import com.ironsource.aura.aircon.compiler.consts.Strings;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElement;
import com.ironsource.aura.aircon.compiler.providerBuilder.generator.ConfigGroupProviderGenerator;
import com.ironsource.aura.aircon.compiler.providerBuilder.generator.ConfigProviderGenerator;
import com.ironsource.aura.aircon.compiler.providerBuilder.generator.ConfigProviderGeneratorFactory;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created on 10/27/2018.
 */
public class RemoteConfigProviderClassBuilder
		extends AbstractConfigClassBuilder {

	private static final String AUX_CLASS_NAME = "Aux";

	private final ProcessingEnvironment mProcessingEnvironment;

	private TypeElement                             mConfigInterfaceElement;
	private List<ConfigProviderGenerator>           mGenerators;
	private Map<ConfigProviderGenerator, ClassName> mProviderGeneratorToProviderClassName;

	private TypeSpec.Builder mTypeSpecBuilder;
	private TypeSpec.Builder mAuxClassTypeSpecBuilder;

	//region constructor
	public RemoteConfigProviderClassBuilder(final ProcessingEnvironment processingEnvironment) {
		super(processingEnvironment);
		mProcessingEnvironment = processingEnvironment;
	}

	//endregion

	//region Build method
	public List<TypeSpec> build(final TypeElement configInterfaceElement) {
		mConfigInterfaceElement = configInterfaceElement;

		mTypeSpecBuilder = createTypeSpecBuilder();

		createGenerators();

		createAuxInnerClass();

		addGetterMethods();

		addAdditionalPredicates();

		addSetterMethods();

		addConfiguredPredicate();

		addDefaultValueGetter();

		addAuxInnerClass();

		final List<TypeSpec> typeSpecs = new ArrayList<>();
		typeSpecs.add(mTypeSpecBuilder.build());
		typeSpecs.addAll(getAdditionalClasses());

		return typeSpecs;
	}

	private void addAuxInnerClass() {
		mTypeSpecBuilder.addType(mAuxClassTypeSpecBuilder.build());
	}

	private void createAuxInnerClass() {
		mAuxClassTypeSpecBuilder = TypeSpec.classBuilder(AUX_CLASS_NAME)
		                                   .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
	}

	private void createGenerators() {
		mGenerators = new ArrayList<>();
		mProviderGeneratorToProviderClassName = new HashMap<>();

		final List<ConfigElement> configElements = mProcessingEnvironment.getClassConfigElements(mConfigInterfaceElement);

		if (configElements == null) {
			return;
		}

		// Handle configs elements with const default value first so their getter
		// will be available
		sortConfigElements(configElements);

		for (ConfigElement configElement : configElements) {
			final ConfigProviderGenerator generator = ConfigProviderGeneratorFactory.create(configElement);
			mGenerators.add(generator);
			mProviderGeneratorToProviderClassName.put(generator, configElement.getProviderClassName());
		}

		final List<ConfigGroupElement> configGroupElements = mProcessingEnvironment.getClassConfigGroupElements(mConfigInterfaceElement);
		if (configGroupElements != null) {
			for (ConfigGroupElement configGroupElement : configGroupElements) {
				final ConfigGroupProviderGenerator generator = new ConfigGroupProviderGenerator(configGroupElement);
				mGenerators.add(generator);
				mProviderGeneratorToProviderClassName.put(generator, configGroupElement.getProviderClassName());
			}
		}
	}

	private void sortConfigElements(final List<ConfigElement> configElements) {
		configElements.sort(new Comparator<ConfigElement>() {
			@Override
			public int compare(final ConfigElement element, final ConfigElement other) {
				if (!element.hasDefaultConfigValue() && other.hasDefaultConfigValue()) {
					return -1;
				}
				else if (element.hasDefaultConfigValue() && !other.hasDefaultConfigValue()) {
					return 1;
				}
				return 0;
			}
		});
	}

	private TypeSpec.Builder createTypeSpecBuilder() {
		return TypeSpec.classBuilder(NamingUtils.getProviderClassName(mConfigInterfaceElement))
		               .addJavadoc(Strings.COMMENT_AUTO_GENERATED)
		               .addModifiers(Modifier.PUBLIC)
		               .addMethod(MethodSpec.constructorBuilder()
		                                    .addModifiers(Modifier.PRIVATE)
		                                    .addComment(Strings.STATIC_CLASS_CONSTRUCTOR)
		                                    .build());
	}

	private void addConfiguredPredicate() {
		for (ConfigProviderGenerator generator : mGenerators) {
			final MethodSpec predicate = generator.createConfiguredPredicate(getAuxClassName(generator));
			if (predicate != null) {
				mAuxClassTypeSpecBuilder.addMethod(predicate);
			}
		}
	}

	private void addGetterMethods() {
		for (ConfigProviderGenerator generator : mGenerators) {
			final MethodSpec rawGetterMethod = generator.createRawGetter(getAuxClassName(generator));
			if (rawGetterMethod != null) {
				mAuxClassTypeSpecBuilder.addMethod(rawGetterMethod);
			}

			final MethodSpec getterMethod = generator.createGetter(getProviderClassName(generator));
			if (getterMethod != null) {
				mTypeSpecBuilder.addMethod(getterMethod);
			}
		}
	}

	private void addAdditionalPredicates() {
		for (ConfigProviderGenerator generator : mGenerators) {
			for (MethodSpec predicate : generator.createAdditionalPredicates(getAuxClassName(generator))) {
				mAuxClassTypeSpecBuilder.addMethod(predicate);
			}
		}
	}

	private void addDefaultValueGetter() {
		for (ConfigProviderGenerator generator : mGenerators) {
			final MethodSpec defaultValueGetter = generator.createDefaultValueGetter(getAuxClassName(generator));
			if (defaultValueGetter != null) {
				mAuxClassTypeSpecBuilder.addMethod(defaultValueGetter);
			}
		}
	}

	private void addSetterMethods() {
		for (ConfigProviderGenerator generator : mGenerators) {
			final MethodSpec setterMethod = generator.createSetter(getProviderClassName(generator));
			if (setterMethod != null) {
				mTypeSpecBuilder.addMethod(setterMethod);
			}
		}
	}

	private List<TypeSpec> getAdditionalClasses() {
		final List<TypeSpec> classes = new ArrayList<>();
		for (ConfigProviderGenerator generator : mGenerators) {
			classes.addAll(generator.createAdditionalClasses());
		}
		return classes;
	}

	private ClassName getAuxClassName(final ConfigProviderGenerator generator) {
		final ClassName providerClassName = getProviderClassName(generator);
		return ClassName.get(providerClassName.packageName(), providerClassName.simpleName() + "." + AUX_CLASS_NAME);
	}

	private ClassName getProviderClassName(final ConfigProviderGenerator generator) {
		return mProviderGeneratorToProviderClassName.get(generator);
	}
}
