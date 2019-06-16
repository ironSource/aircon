package com.ironsource.aura.aircon.sample;

import com.ironsource.aura.aircon.common.ConfigTypeResolver;
import com.ironsource.aura.aircon.sample.config.model.Label;

import java.util.Arrays;

public class LabelConfigResolver
		implements ConfigTypeResolver<LabelConfig, String, Label> {

	@Override
	public Class<LabelConfig> getAnnotationClass() {
		return LabelConfig.class;
	}

	@Override
	public boolean isValid(final LabelConfig annotation, final String value) {
		final String[] invalidValues = annotation.invalidValues();
		return !Arrays.asList(invalidValues)
		              .contains(value);
	}

	@Override
	public Label process(final LabelConfig annotation, final String value) {
		return Label.from(value);
	}
}
