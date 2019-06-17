package com.ironsource.aura.aircon.common;

import java.lang.annotation.Annotation;

/**
 * An interface defining all required logic for resolving a custom config type.
 *
 * @param <A> annotation type this config resolver is defined for, this value must be the same as the one supplied as parameter in the {@link ConfigType} annotation itself
 * @param <T> config primitive type, meaning the raw type that will be configured remotely, supported types are: String, float, int, long and boolean.
 * @param <S> config type, meaning the type the config provider will return after {@link #process(Annotation, T)} is invoked on the configured value
 */
public interface ConfigTypeResolver <A extends Annotation, T, S> {

	/**
	 * Return the annotation type this config resolver is defined for.
	 * This value must be the same as the one supplied as parameter in the {@link ConfigType} annotation itself
	 *
	 * @return annotation class
	 */
	Class<A> getAnnotationClass();

	/**
	 * Returns whether the configured value is valid.
	 * This method is invoked by the config provider to check if value is valid or should fallback to default value.
	 * @param annotation annotation instance used on the config field
	 * @param value configured value
	 * @return true if value is valid, false otherwise
	 */
	boolean isValid(A annotation, T value);

	/**
	 * Process the configured value.
	 * This method is invoked before the config provider returns the configured value.
	 * @param annotation annotation instance used on the config field
	 * @param value configured value
	 * @return configured value after processing
	 */
	S process(A annotation, T value);
}
