package com.ironsource.aura.aircon.compiler.utils;


import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Hanan on 2/13/2017.
 */

public class Utils {

	public static Set<String> toAnnotationSet(Class<? extends Annotation>... vars) {
		Set<String> set = new LinkedHashSet<>();
		for (Class<? extends Annotation> var : vars) {
			set.add(var.getCanonicalName());
		}
		return set;
	}
}
