package com.ironsource.aura.aircon.sample;

import com.ironsource.aura.aircon.common.ConfigTypeResolver;
import com.ironsource.aura.aircon.sample.config.model.Label;

import java.util.Arrays;
import java.util.List;

public class StringListConfigResolver
        implements ConfigTypeResolver<StringListConfig, Object, List<String>> {

    @Override
    public Class<StringListConfig> getAnnotationClass() {
        return StringListConfig.class;
    }

    @Override
    public boolean isValid(final StringListConfig annotation, final Object value) {
        return true;
    }

    @Override
    public List<String> process(final StringListConfig annotation, final Object value) {
        return (List<String>) value;
    }
}