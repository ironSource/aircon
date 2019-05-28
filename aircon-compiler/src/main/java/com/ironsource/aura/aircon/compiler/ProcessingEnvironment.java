package com.ironsource.aura.aircon.compiler;

import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElement;
import com.ironsource.aura.aircon.compiler.utils.ProcessingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created on 11/11/2018.
 */
public class ProcessingEnvironment {

	private final ProcessingUtils mProcessingUtils;

	private final List<ConfigElement>                   mConfigElements;
	private final Map<TypeElement, List<ConfigElement>> mClassToConfigElements;

	private final List<ConfigGroupElement>                   mConfigGroupElements;
	private final Map<TypeElement, List<ConfigGroupElement>> mClassToConfigGroupElements;

	ProcessingEnvironment(final ProcessingUtils processingUtils) {
		mProcessingUtils = processingUtils;
		mConfigElements = new ArrayList<>();
		mConfigGroupElements = new ArrayList<>();
		mClassToConfigElements = new HashMap<>();
		mClassToConfigGroupElements = new HashMap<>();
	}

	public Types getTypeUtils() {
		return mProcessingUtils.getTypeUtils();
	}

	public Elements getElementUtils() {
		return mProcessingUtils.getElementUtils();
	}

	public ProcessingUtils.Logger getLogger() {
		return mProcessingUtils.getLogger();
	}

	public List<ConfigElement> getClassConfigElements(TypeElement classElement) {
		return mClassToConfigElements.get(classElement);
	}

	public List<ConfigGroupElement> getClassConfigGroupElements(TypeElement classElement) {
		return mClassToConfigGroupElements.get(classElement);
	}

	public List<ConfigElement> getConfigElements() {
		return mConfigElements;
	}

	public List<ConfigGroupElement> getConfigGroupElements() {
		return mConfigGroupElements;
	}

	void addConfigElement(TypeElement configClass, ConfigElement configElement) {
		mConfigElements.add(configElement);
		List<ConfigElement> configElements = mClassToConfigElements.get(configClass);
		if (configElements == null) {
			configElements = new ArrayList<>();
			mClassToConfigElements.put(configClass, configElements);
		}
		configElements.add(configElement);
	}

	void addConfigGroupElement(TypeElement configClass, ConfigGroupElement configGroupElement) {
		mConfigGroupElements.add(configGroupElement);
		List<ConfigGroupElement> configGroupElements = mClassToConfigGroupElements.get(configClass);
		if (configGroupElements == null) {
			configGroupElements = new ArrayList<>();
			mClassToConfigGroupElements.put(configClass, configGroupElements);
		}
		configGroupElements.add(configGroupElement);
	}
}