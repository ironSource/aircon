package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.annotations.config.ConfigGroup;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created on 21/1/19.
 */
public class ConfigGroupElementFactory {

	public static ConfigGroupElement createFeatureConfigGroupElement(final TypeElement configClass, final ClassName providerClassName, final List<ConfigElement> configElements) {
		final String name = NamingUtils.getFeatureConfigGroupName(configClass);
		return createConfigGroupElement(providerClassName, configClass, configElements, name, name, true);
	}

	public static ConfigGroupElement createConfigGroupElement(final ClassName providerClassName, final TypeElement configClass, final VariableElement variableElement, final ConfigGroup configGroupAnnotation, final List<ConfigElement> configElements, final List<ConfigGroupElement> configGroupElements) {
		final Set<AbstractConfigElement> groupConfigElements = new HashSet<>();
		final String[] configs = configGroupAnnotation.value();
		for (String config : configs) {
			final ConfigElement configElement = AbstractConfigElement.getConfigElementByKey(config, configElements);
			if (configElement != null) {
				groupConfigElements.add(configElement);
			}
			else {
				final ConfigGroupElement configGroupElement = AbstractConfigElement.getConfigElementByKey(config, configGroupElements);
				groupConfigElements.add(configGroupElement);
			}
		}
		final String name = variableElement.getSimpleName()
		                                   .toString();
		final String groupName = (String) variableElement.getConstantValue();

		return createConfigGroupElement(providerClassName, configClass, groupConfigElements, name, groupName, false);
	}

	private static ConfigGroupElement createConfigGroupElement(final ClassName providerClassName, final TypeElement configClass, final Collection<? extends AbstractConfigElement> configElements, final String name, final String groupName, final boolean featureGroup) {
		final TypeName typeName = ClassName.get(providerClassName.packageName(), NamingUtils.getConfigGroupClassName(groupName));
		return new ConfigGroupElement(new ConfigGroupElement.Properties(name, groupName, typeName, providerClassName, TypeName.get(configClass.asType())), new ArrayList<>(configElements), featureGroup);
	}
}
