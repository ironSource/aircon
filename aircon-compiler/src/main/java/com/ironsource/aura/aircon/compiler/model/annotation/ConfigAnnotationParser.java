package com.ironsource.aura.aircon.compiler.model.annotation;

import com.squareup.javapoet.TypeName;

public interface ConfigAnnotationParser {

	ConfigKind getKind();

	TypeName getAnnotationType();

	<T> T getAttribute(String attr);

	TypeName getRawType();

	TypeName getType();
}
