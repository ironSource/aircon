package com.ironsource.aura.aircon.compiler.providerBuilder.generator;

import com.ironsource.aura.aircon.compiler.ProcessingEnvironment;
import com.ironsource.aura.aircon.compiler.consts.Consts;
import com.ironsource.aura.aircon.compiler.descriptors.AirConUtilsClassDescriptor;
import com.ironsource.aura.aircon.compiler.descriptors.ClassDescriptor;
import com.ironsource.aura.aircon.compiler.model.element.EnumConfigElement;
import com.ironsource.aura.aircon.compiler.utils.CodeBlockBuilder;
import com.ironsource.aura.aircon.compiler.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;


/**
 * Created on 11/7/2018.
 */
class EnumConfigProviderGenerator
        extends DefaultConfigProviderGenerator<EnumConfigElement> {

    private final ProcessingEnvironment mProcessingEnvironment;

    EnumConfigProviderGenerator(ProcessingEnvironment processingEnvironment, final EnumConfigElement configElement) {
        super(configElement);
        mProcessingEnvironment = processingEnvironment;
    }

    @Override
    public CodeBlock getConversionToTypeExpression(final Object varDefaultValue, final Object varValue) {
        final CodeBlock enumValue = new CodeBlockBuilder().addClassQualifier(getEnumsProviderClassName())
                .addMethodCall(getEnumProviderMethodName(), varValue, varDefaultValue)
                .build();
        if (mElement.hasRandomizerValue()) {
            final CodeBlock condition = new CodeBlockBuilder().addBinaryOperator(CodeBlockBuilder.OPERATOR_EQUALITY, varValue, mElement.getRandomizerValue())
                    .build();

            final CodeBlock enumClass = ClassDescriptor.clazz(TypeName.get(mElement.getEnumClass()))
                    .build();
            final CodeBlock randomEnumValue = AirConUtilsClassDescriptor.getRandomEnumValue(getKeyParam(), enumClass)
                    .build();

            return new CodeBlockBuilder().addConditionalExpression(condition, randomEnumValue, enumValue)
                    .build();
        } else {
            return enumValue;
        }
    }

    private String getEnumProviderMethodName() {
        return Consts.GETTER_METHOD_PREFIX + NamingUtils.getSimpleName(TypeName.get(mElement.getEnumClass()));
    }

    @Override
    protected CodeBlock getConversionToRawTypeExpression(final Object value) {
        return new CodeBlockBuilder().addClassQualifier(getEnumsProviderClassName())
                .addMethodCall(NamingUtils.ENUMS_PROVIDER_REMOTE_VALUE_GETTER_METHOD, value)
                .build();
    }

    private ClassName getEnumsProviderClassName() {
        return ClassName.get(mProcessingEnvironment.getBasePackage(), NamingUtils.ENUMS_PROVIDER_CLASS_NAME);
    }
}
