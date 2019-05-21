package com.ironsource.aura.aircon.sample.config.model;

import com.ironsource.aura.aircon.common.annotations.config.value.RemoteStringValue;

/**
 * Created on 11/24/2018.
 */
public enum ImageLocation {
	@RemoteStringValue("bottom") BOTTOM,
	@RemoteStringValue("center") CENTER,
	@RemoteStringValue("top") TOP
}
