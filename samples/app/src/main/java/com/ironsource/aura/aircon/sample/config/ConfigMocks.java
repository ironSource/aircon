package com.ironsource.aura.aircon.sample.config;

import android.annotation.SuppressLint;

import com.ironsource.aura.aircon.EnumsProvider;
import com.ironsource.aura.aircon.common.annotations.ConfigMock;
import com.ironsource.aura.aircon.sample.config.model.TextLocation;

/**
 * Created on 7/3/19.
 */
@SuppressLint("MissingAnnotation")
public class ConfigMocks {

	@ConfigMock(RemoteConfigs.SomeCoolFeature.TEXT_LOCATION)
	public static int mockTextLocation() {
		return EnumsProvider.getRemoteValue(TextLocation.CENTER);
	}

//	@ConfigMock(RemoteConfigs.SomeCoolFeature.SOME_CUSTOM_MAP)
//	public static String mockSomeCustomMap() {
//		return "{\"hello\":\"world\"}";
//	}
}