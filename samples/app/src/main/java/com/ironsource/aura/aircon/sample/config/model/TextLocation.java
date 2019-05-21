package com.ironsource.aura.aircon.sample.config.model;

import com.ironsource.aura.aircon.common.annotations.config.value.RemoteIntValue;

/**
 * Created on 11/24/2018.
 */
public enum TextLocation {
	@RemoteIntValue(0) BOTTOM,
	@RemoteIntValue(1) CENTER,
	@RemoteIntValue(2) TOP
}
