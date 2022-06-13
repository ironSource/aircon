package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.ProcessingEnvironment;
import com.ironsource.aura.aircon.compiler.model.element.ColorConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.ConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.CustomConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.EnumConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.JsonConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.NumberConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.PrimitiveConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.StringConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.StringSetConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.StyledTextConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.TextConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.TimeConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.UrlConfigElement;
import com.ironsource.aura.aircon.compiler.model.element.Visitor;

/**
 * Created on 11/7/2018.
 */
public class ConfigProviderGeneratorFactory {

    public static ConfigProviderGenerator create(final ProcessingEnvironment processingEnvironment, ConfigElement configElement) {
        return configElement.accept(new Visitor<Void, ConfigProviderGenerator>() {
            @Override
            public ConfigProviderGenerator visit(final PrimitiveConfigElement configElement, final Void arg) {
                return new PrimitiveConfigProviderGenerator<>(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final NumberConfigElement configElement, final Void arg) {
                return new NumberConfigProviderGenerator<>(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final StringConfigElement configElement, final Void arg) {
                return new StringConfigProviderGenerator<>(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final TextConfigElement configElement, final Void arg) {
                return new StringConfigProviderGenerator<>(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final StringSetConfigElement configElement, final Void arg) {
                return new StringSetConfigProviderGenerator(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final TimeConfigElement configElement, final Void arg) {
                return new TimeConfigProviderGenerator(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final ColorConfigElement configElement, final Void arg) {
                return new ColorConfigProviderGenerator(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final UrlConfigElement configElement, final Void arg) {
                return new UrlConfigProviderGenerator(configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final EnumConfigElement configElement, final Void arg) {
                return new EnumConfigProviderGenerator(processingEnvironment, configElement);
            }

            @Override
            public ConfigProviderGenerator visit(final JsonConfigElement configElement, final Void arg) {
                return new JsonConfigProviderGenerator(configElement);
            }

			@Override
			public ConfigProviderGenerator visit(final CustomConfigElement configElement, final Void arg) {
				return new CustomConfigProviderGenerator(configElement);
			}

			@Override
			public ConfigProviderGenerator visit(final StyledTextConfigElement configElement, final Void arg) {
				return new StyledTextConfigProviderGenerator(configElement);
			}
		}, null);
	}
}
