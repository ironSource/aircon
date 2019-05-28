package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.model.element.PrimitiveConfigElement;


class PrimitiveConfigProviderGenerator <T extends PrimitiveConfigElement>
		extends DefaultConfigProviderGenerator<T> {
	
	PrimitiveConfigProviderGenerator(final T element) {
		super(element);
	}
}