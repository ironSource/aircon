package com.ironsource.aura.aircon.compiler;

/**
 * Created on 11/6/2018.
 */
public abstract class AbstractConfigClassBuilder {

	protected final ProcessingEnvironment mProcessingEnvironment;

	public AbstractConfigClassBuilder(final ProcessingEnvironment processingEnvironment) {
		mProcessingEnvironment = processingEnvironment;
	}
}
