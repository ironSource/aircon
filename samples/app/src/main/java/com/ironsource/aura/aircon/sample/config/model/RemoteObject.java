package com.ironsource.aura.aircon.sample.config.model;

import com.google.gson.annotations.SerializedName;

public class RemoteObject {

	@SerializedName("int") private int mInt;

	@SerializedName("str") private String mStr;

	public int getInt() {
		return mInt;
	}

	public String getStr() {
		return mStr;
	}
}
