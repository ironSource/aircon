package com.ironsource.aura.aircon;

public interface JsonConverter {

	String toJson(Object obj);

	<T> T fromJson(String json, Class<T> clazz) throws
			JsonException;

	class JsonException
			extends Exception {

		public JsonException() {
		}

		public JsonException(final String message) {
			super(message);
		}

		public JsonException(final String message, final Throwable cause) {
			super(message, cause);
		}

		public JsonException(final Throwable cause) {
			super(cause);
		}
	}
}
