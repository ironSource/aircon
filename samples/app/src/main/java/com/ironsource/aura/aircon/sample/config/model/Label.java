package com.ironsource.aura.aircon.sample.config.model;

public class Label {

	private final String mStr;

	private Label(final String str) {
		mStr = str;
	}

	public String getStr() {
		return mStr;
	}

	public static Label from(final String val) {
		return new Label(val);
	}
}
