package com.ironsource.aura.aircon.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 11/12/2018.
 */
public class Utils {

	public static <T> List<T> concatLists(List<T>... lists) {
		final List<T> resList = new ArrayList<>();
		for (List<T> list : lists) {
			resList.addAll(list);
		}
		return resList;
	}
}
