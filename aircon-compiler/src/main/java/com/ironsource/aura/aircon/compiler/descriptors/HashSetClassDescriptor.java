package com.ironsource.aura.aircon.compiler.descriptors;

import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;

/**
 * Created on 25/12/18.
 */
public class HashSetClassDescriptor
		extends ClassDescriptor {

	private static final ClassName CLASS_NAME = ClassName.get(HashSet.class);

	HashSetClassDescriptor(final CodeBlockBuilder builder) {
		super(builder);
	}

	public static HashSetClassDescriptor newInstance(TypeName typeName) {
		return new HashSetClassDescriptor(new CodeBlockBuilder().addConstructorCall(getTypeName(typeName)));
	}

	public static HashSetClassDescriptor from(TypeName typeName, Object collection) {
		return new HashSetClassDescriptor(new CodeBlockBuilder().addConstructorCall(getTypeName(typeName), collection));
	}

	private static TypeName getTypeName(final TypeName typeName) {
		return ParameterizedTypeName.get(CLASS_NAME, typeName);
	}
}
