package com.ironsource.aura.aircon.compiler.model.element;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;

/**
 * Created on 11/3/2018.
 */
public class NumberConfigElement
		extends PrimitiveConfigElement {

	private final Number              mMinValue;
	private final Number              mMaxValue;
	private final RangeFallbackPolicy mMinValueFallbackPolicy;
	private final RangeFallbackPolicy mMaxValueFallbackPolicy;

	NumberConfigElement(Properties properties, final Number minValue, final Number maxValue, final RangeFallbackPolicy minValueFallbackPolicy, final RangeFallbackPolicy maxValueFallbackPolicy) {
		super(properties);
		mMinValue = minValue;
		mMaxValue = maxValue;
		mMinValueFallbackPolicy = minValueFallbackPolicy;
		mMaxValueFallbackPolicy = maxValueFallbackPolicy;
	}

	public boolean hasMinValue() {
		return mMinValue != null;
	}

	public boolean hasMaxValue() {
		return mMaxValue != null;
	}

	public Number getMinValue() {
		return mMinValue;
	}

	public Number getMaxValue() {
		return mMaxValue;
	}

	public RangeFallbackPolicy getMinValueFallbackPolicy() {
		return mMinValueFallbackPolicy;
	}

	public RangeFallbackPolicy getMaxValueFallbackPolicy() {
		return mMaxValueFallbackPolicy;
	}

	@Override
	public <T, S> S accept(final Visitor<T, S> visitor, final T arg) {
		return visitor.visit(this, arg);
	}
}
