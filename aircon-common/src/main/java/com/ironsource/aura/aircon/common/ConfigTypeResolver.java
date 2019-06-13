package com.ironsource.aura.aircon.common;

public interface ConfigTypeResolver <T, S> {

	boolean isValid(T value);

	S process(T value);
}
