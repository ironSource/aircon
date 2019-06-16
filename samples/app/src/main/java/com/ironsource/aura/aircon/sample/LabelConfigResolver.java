package com.ironsource.aura.aircon.sample;

import android.text.TextUtils;

import com.ironsource.aura.aircon.common.ConfigTypeResolver;
import com.ironsource.aura.aircon.sample.config.model.Label;

public class LabelConfigResolver
		implements ConfigTypeResolver<String, Label> {

	@Override
	public boolean isValid(final String value) {
		return !TextUtils.isEmpty(value);
	}

	@Override
	public Label process(final String value) {
		return Label.from(value);
	}
}
