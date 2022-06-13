package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;

public class HtmlClassDescriptor
		extends ClassDescriptor {

	private static final String PACKAGE = "android.text";

	public static final ClassName CLASS_NAME = ClassName.get(PACKAGE, "Html");

	private interface StaticMethods {

		String FROM_HTML = "fromHtml";
	}

	private HtmlClassDescriptor(CodeBlockBuilder builder) {
		super(builder);
	}

	public static StubClassDescriptor fromHtml(Object source) {
		return new StubClassDescriptor(staticMethodCall(CLASS_NAME, StaticMethods.FROM_HTML, source));
	}
}
