package com.ironsource.aura.aircon.sample;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ironsource.aura.aircon.common.ConfigTypeResolver;

import java.util.Map;

public class MapConfigTypeResolver
		implements ConfigTypeResolver<String, Map<String, String>> {

	private final Gson mGson;

	public MapConfigTypeResolver(final Gson gson) {
		mGson = gson;
	}

	@Override
	public boolean isValid(final String value) {
		return !TextUtils.isEmpty(value);
	}

	@Override
	public Map<String, String> process(final String value) {
		return mGson.fromJson(value, new TypeToken<Map<String, String>>() {}.getType());
	}
}
