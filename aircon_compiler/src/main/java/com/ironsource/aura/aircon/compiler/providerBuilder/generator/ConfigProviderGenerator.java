package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

/**
 * Created on 11/7/2018.
 */
public interface ConfigProviderGenerator {

	MethodSpec createGetter(final ClassName className);

	MethodSpec createRawGetter(final ClassName className);

	MethodSpec createDefaultValueGetter(final ClassName className);

	MethodSpec createConfiguredPredicate(final ClassName className);

	List<MethodSpec> createAdditionalPredicates(final ClassName className);

	MethodSpec createSetter(final ClassName className);

	List<TypeSpec> createAdditionalClasses();
}
