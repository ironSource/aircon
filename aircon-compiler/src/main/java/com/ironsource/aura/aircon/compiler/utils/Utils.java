package com.ironsource.aura.aircon.compiler.utils;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor6;

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

	public static List<TypeMirror> getGenericTypes(final TypeMirror type) {
		final List<TypeMirror> result = new ArrayList<>();

		type.accept(new SimpleTypeVisitor6<Void, Void>() {
			@Override
			public Void visitDeclared(DeclaredType declaredType, Void v) {
				result.addAll(declaredType.getTypeArguments());
				return null;
			}

			@Override
			public Void visitPrimitive(PrimitiveType primitiveType, Void v) {
				return null;
			}

			@Override
			public Void visitArray(ArrayType arrayType, Void v) {
				return null;
			}

			@Override
			public Void visitTypeVariable(TypeVariable typeVariable, Void v) {
				return null;
			}

			@Override
			public Void visitError(ErrorType errorType, Void v) {
				return null;
			}

			@Override
			protected Void defaultAction(TypeMirror typeMirror, Void v) {
				throw new UnsupportedOperationException();
			}
		}, null);

		return result;
	}
}
