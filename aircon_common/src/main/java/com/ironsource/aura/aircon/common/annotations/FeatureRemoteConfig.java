package com.ironsource.aura.aircon.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark an interface as a remote configs interface.
 * Only interfaces annotated with this annotation are taken into account during code generation phase.
 * For every interface annotated by this annotation a config provider class is generated with
 * the name of the interface and a "ConfigProvider" suffix. (e.g MyFeature interface -> MyFeatureConfigProvider).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface FeatureRemoteConfig {

	/**
	 * Whether to create a group containing all the configs defined in the annotated interface.
	 * @see com.ironsource.aura.aircon.common.annotations.config.ConfigGroup
	 */
	boolean createGroup() default false;
}
