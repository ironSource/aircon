package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;

import java.util.concurrent.TimeUnit;

/**
 * Created on 11/3/2018.
 */
public class TimeConfigElement
		extends NumberConfigElement {

	private final TimeUnit mDefaultValueTimeUnit;

	TimeConfigElement(Properties properties, final Number minValue, final Number maxValue, final RangeFallbackPolicy minValueFallbackPolicy, final RangeFallbackPolicy maxValueFallbackPolicy, TimeUnit defaultValueTimeUnit) {
		super(properties, minValue, maxValue, minValueFallbackPolicy, maxValueFallbackPolicy);
		mDefaultValueTimeUnit = defaultValueTimeUnit;
	}

	public TimeUnit getDefaultValueTimeUnit() {
		return mDefaultValueTimeUnit;
	}

	@Override
	public <T, S> S accept(final Visitor<T, S> visitor, final T arg) {
		return visitor.visit(this, arg);
	}
}
