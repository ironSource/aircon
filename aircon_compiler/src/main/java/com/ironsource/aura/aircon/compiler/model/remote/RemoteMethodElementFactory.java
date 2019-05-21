package com.ironsource.aura.aircon.compiler.model.remote;

import com.ironsource.aura.aircon.common.annotations.injection.RemoteApiMethod;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteFlag;
import com.ironsource.aura.aircon.common.annotations.injection.RemoteParam;
import com.ironsource.aura.aircon.compiler.model.element.AbstractConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigGroupElement;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Created on 11/3/2018.
 */
public class RemoteMethodElementFactory {

	public static RemoteMethodElement create(ExecutableElement element, List<ConfigElement> configElements, final List<ConfigGroupElement> configGroupElements) {
		final String methodName = element.getSimpleName()
		                                 .toString();
		final String className = element.getEnclosingElement()
		                                .getSimpleName()
		                                .toString();

		final RemoteFlag flagAnnotation = getRemoteFlagAnnotation(element);

		final ConfigElement flagConfigElement = flagAnnotation != null ? AbstractConfigElement.getConfigElementByKey(flagAnnotation.value(), configElements) : null;

		final List<AbstractConfigElement> remoteParameters = new ArrayList<>();

		final List<VariableElement> parameters = new ArrayList<>();

		for (VariableElement parameter : element.getParameters()) {
			if (isRemoteParam(parameter)) {
				final RemoteParam remoteParamAnnotation = getRemoteParamAnnotation(parameter);
				final ConfigElement configElement = AbstractConfigElement.getConfigElementByKey(remoteParamAnnotation.value(), configElements);
				if (configElement != null) {
					remoteParameters.add(configElement);
				}
				else {
					final ConfigGroupElement configGroupElement = AbstractConfigElement.getConfigElementByKey(remoteParamAnnotation.value(), configGroupElements);
					if (configGroupElement != null) {
						remoteParameters.add(configGroupElement);
					}
				}
			}
			else {
				parameters.add(parameter);
			}
		}

		return new RemoteMethodElement(methodName, className, hasRemoteApiMethodAnnotation(element), flagConfigElement, parameters, remoteParameters,element.getReturnType());
	}

	private static boolean isRemoteParam(final VariableElement element) {
		return getRemoteParamAnnotation(element) != null;
	}

	private static RemoteParam getRemoteParamAnnotation(final VariableElement element) {
		return element.getAnnotation(RemoteParam.class);
	}

	private static RemoteFlag getRemoteFlagAnnotation(final ExecutableElement element) {
		return element.getAnnotation(RemoteFlag.class);
	}

	private static boolean hasRemoteApiMethodAnnotation(final ExecutableElement element) {
		return element.getAnnotation(RemoteApiMethod.class) != null;
	}
}
