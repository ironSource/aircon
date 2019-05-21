package com.ironsource.aura.aircon.common;

/**
 * Define a policy to use when a config value is out of range (min/max values)
 */
public enum RangeFallbackPolicy {
	/**
	 * If value is out of range fallback to default value.
	 */
	DEFAULT,
	/**
	 * If value is out of range round to range max/min value.
	 */
	RANGE_VALUE
}