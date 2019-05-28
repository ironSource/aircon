package com.ironsource.aura.aircon.compiler.utils;


import com.ironsource.aura.aircon.compiler.model.generated.GeneratedMethod;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 11/17/2018.
 */
public class CodeBlockBuilder {

	public static final String NULL = "null";

	public static final String CLASS = "class";

	public static final String OPERATOR_GREATER_THAN           = ">";
	public static final String OPERATOR_GREATER_THAN_OR_EQUALS = ">=";
	public static final String OPERATOR_MINUS                  = "-";
	public static final String OPERATOR_EQUALITY               = "==";
	public static final String OPERATOR_UNEQUALITY             = "!=";

	private static final String OPERATOR_NOT = "!";

	private static final String METHOD_SUPER = "super";

	private static final String FLOAT_CASTER  = "F";
	private static final String DOUBLE_CASTER = "D";
	private static final String LONG_CASTER   = "L";

	private CodeBlock.Builder mBuilder;

	public CodeBlockBuilder() {
		mBuilder = CodeBlock.builder();
	}

	public CodeBlockBuilder beginSwitch(Object value) {
		add("switch (");
		add(value);
		add(")");
		beginControlFlow("");
		indent();
		return this;
	}

	public CodeBlockBuilder addDefaultSwitchCase(CodeBlock codeBlock) {
		return addSwitchCase(null, codeBlock);
	}

	public CodeBlockBuilder addSwitchCase(Object value, CodeBlock code) {
		if (value != null) {
			add("case ");
			add(value);
		}
		else {
			add("default");
		}
		add(":");
		indent();
		add(code);
		unindent();
		return this;
	}

	public CodeBlockBuilder endSwitch() {
		unindent();
		endControlFlow();
		return this;
	}

	public CodeBlockBuilder addNot() {
		add(OPERATOR_NOT);
		return this;
	}

	public CodeBlockBuilder addBinaryOperator(String operator, Object val1, Object val2) {
		add(val1);
		add(" $L ", operator);
		add(val2);
		return this;
	}

	public CodeBlockBuilder addAnd(Object... vals) {
		addDelimiterExpression(" && ", vals);
		return this;
	}

	public CodeBlockBuilder addOr(Object... vals) {
		addDelimiterExpression(" || ", vals);
		return this;
	}

	public CodeBlockBuilder beginIf(Object condition) {
		add("if (");
		add(condition);
		add(")");
		beginControlFlow("");
		indent();
		return this;
	}

	public CodeBlockBuilder endIf() {
		unindent();
		endControlFlow();
		return this;
	}

	public CodeBlockBuilder beginElse() {
		beginControlFlow("else");
		indent();
		return this;
	}

	public CodeBlockBuilder endElse() {
		unindent();
		endControlFlow();
		return this;
	}

	public CodeBlockBuilder addCast(TypeName className, Object var) {
		return add("($T) ", className).add(var);
	}

	public CodeBlockBuilder addPrimitiveCaster(TypeName className, Object var) {
		String caster = "";
		if (className == TypeName.FLOAT) {
			caster = FLOAT_CASTER;
		}
		else if (className == TypeName.DOUBLE) {
			caster = DOUBLE_CASTER;
		}
		else if (className == TypeName.LONG) {
			caster = LONG_CASTER;
		}
		return add(var).add(caster);
	}

	public CodeBlockBuilder addReturn(Object value) {
		return add("return ").add(value)
		                     .endStatement();
	}

	// TODO - add support for $C for CodeBlock
	public CodeBlockBuilder addConditionalExpression(Object condition, Object arg1, Object arg2) {
		return add(condition).add(" ? ")
		                     .add(arg1)
		                     .add(" : ")
		                     .add(arg2);

	}

	public CodeBlockBuilder addAssignmentExpression(String var, Object value) {
		return add(var).add(" = ")
		               .add(value)
		               .endStatement();

	}

	public CodeBlockBuilder addLocalVariable(TypeName className, String name, Object value, boolean makeFinal) {
		if (makeFinal) {
			add("final ");
		}
		return add("$T $L = ", className, name).add(value)
		                                       .endStatement();
	}

	public CodeBlockBuilder addConstructorCall(TypeName className, Collection<?> params) {
		return addConstructorCall(className, params.toArray());
	}

	public CodeBlockBuilder addConstructorCall(TypeName className, Object... params) {
		return add("new $T", className).addMethodCall("", params);
	}

	public CodeBlockBuilder addGeneratedMethodCall(final GeneratedMethod method) {
		return addGeneratedMethodCall(method, new HashMap<String, Object>());
	}

	public CodeBlockBuilder addGeneratedMethodCall(final GeneratedMethod method, Map<String, Object> values) {
		final List<Object> params = new ArrayList<>();

		final List<GeneratedMethod.Parameter> parameters = method.getParameters();
		for (GeneratedMethod.Parameter parameter : parameters) {
			final Object paramValue = values.get(parameter.getName());
			params.add(paramValue != null ? paramValue : parameter.getName());
		}

		return addStaticMethodCall(method.getClassName(), method.getName(), params);
	}

	public CodeBlockBuilder addSuperMethodCall(Collection<?> params) {
		return addMethodCall(METHOD_SUPER, params.toArray());
	}

	public CodeBlockBuilder addStaticMethodCall(TypeName className, String methodName, Collection<?> params) {
		return addStaticMethodCall(className, methodName, params.toArray());
	}

	public CodeBlockBuilder addStaticMethodCall(TypeName className, String methodName, Object... params) {
		add("$T.", className);
		return addMethodCall(methodName, params);
	}

	public CodeBlockBuilder addMethodCall(Object methodName, Collection<?> params) {
		return addMethodCall(methodName, params.toArray());
	}

	public CodeBlockBuilder addMethodCall(Object methodName, Object... params) {
		add(methodName);
		add("(");
		addDelimiterExpression(", ", params);
		add(")");
		return this;
	}

	private void addDelimiterExpression(String delimiter, final Object... params) {
		for (int i = 0 ; i < params.length ; i++) {
			final Object parameter = params[i];
			add(parameter);
			if (i < params.length - 1) {
				add(delimiter);
			}
		}
	}

	public CodeBlockBuilder addClassQualifier(final TypeName className) {
		add("$T.", className);
		return this;
	}


	public CodeBlockBuilder add(final Object value) {
		if (value instanceof CodeBlock) {
			return add((CodeBlock) value);
		}
		else {
			return add("$L", value);
		}
	}

	public CodeBlockBuilder add(final String format, final Object... args) {
		mBuilder.add(format, args);
		return this;
	}

	public CodeBlockBuilder endStatement() {
		addStatement("");
		return this;
	}

	public CodeBlockBuilder addNamed(final String format, final Map<String, ?> arguments) {
		mBuilder.addNamed(format, arguments);
		return this;
	}

	public CodeBlockBuilder add(final CodeBlock codeBlock) {
		if (codeBlock != null) {
			mBuilder.add(codeBlock);
		}
		return this;
	}

	public CodeBlockBuilder addStatement(final String format, final Object... args) {
		mBuilder.addStatement(format, args);
		return this;
	}

	public CodeBlockBuilder beginControlFlow(final String controlFlow, final Object... args) {
		mBuilder.beginControlFlow(controlFlow, args);
		return this;
	}

	public CodeBlockBuilder nextControlFlow(final String controlFlow, final Object... args) {
		mBuilder.nextControlFlow(controlFlow, args);
		return this;
	}

	public CodeBlockBuilder endControlFlow() {
		mBuilder.endControlFlow();
		return this;
	}

	public CodeBlockBuilder endControlFlow(final String controlFlow, final Object... args) {
		mBuilder.endControlFlow(controlFlow, args);
		return this;
	}

	public CodeBlockBuilder indent() {
		mBuilder.indent();
		return this;
	}

	public CodeBlockBuilder unindent() {
		mBuilder.unindent();
		return this;
	}

	public CodeBlock build() {
		return mBuilder.build();
	}

	public static CodeBlockBuilder of(Object obj) {
		return new CodeBlockBuilder().add(obj);
	}

	public static CodeBlockBuilder of(String format, Object... args) {
		return new CodeBlockBuilder().add(format, args);
	}
}
