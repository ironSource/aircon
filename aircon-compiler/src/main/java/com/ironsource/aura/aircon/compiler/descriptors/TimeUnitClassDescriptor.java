package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.TypeName;

import java.util.concurrent.TimeUnit;

/**
 * Created on 11/17/2018.
 */
public class TimeUnitClassDescriptor
		extends ClassDescriptor {

	private interface Methods {

		String TO_MILLIS = "toMillis";
	}

	TimeUnitClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public StubClassDescriptor toMillis(Object value) {
		addMethodCall(Methods.TO_MILLIS, value);
		return new StubClassDescriptor(mBuilder);
	}

	public static TimeUnitClassDescriptor newInstance(TimeUnit timeUnit) {
		return new TimeUnitClassDescriptor(new CodeBlockBuilder().addClassQualifier(TypeName.get(TimeUnit.class))
		                                                         .add(timeUnit));
	}
}