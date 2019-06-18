package com.ironsource.aura.aircon.converter.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ironsource.aura.aircon.JsonConverter;

import java.lang.reflect.Type;

public class GsonConverter
		implements JsonConverter {

	private Gson mGson;

	public GsonConverter() {
		this(new Gson());
	}

	public GsonConverter(final Gson gson) {
		mGson = gson;
	}

	@Override
	public String toJson(final Object obj) {
		return mGson.toJson(obj);
	}

	@Override
	public <T> T fromJson(final String json, final Class<T> clazz) throws
			JsonException {
		try {
			return mGson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public <T> T fromJson(final String json, final Type type) throws
			JsonException {
		try {
			return mGson.fromJson(json, type);
		} catch (JsonSyntaxException e) {
			throw new JsonException(e);
		}
	}
}
