package com.ironsource.aura.aircon.lint;

import com.android.tools.lint.detector.api.JavaContext;
import com.ironsource.aura.aircon.lint.detector.DuplicateFeatureRemoteDetector;
import com.ironsource.aura.aircon.lint.detector.FeatureRemoteConfigOnClassDetector;
import com.ironsource.aura.aircon.lint.detector.IssueDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.ConfigAttributeAnnotationOnNonConfigFieldDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.auxMethod.AuxMethodInvalidSignatureDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.auxMethod.ConfigMockProtectionDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.auxMethod.ConfigValidatorInvalidSignature;
import com.ironsource.aura.aircon.lint.detector.annotation.auxMethod.NonConfigAuxMethodAnnotationValueDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.enums.InconsistentRemoteValueAnnotationsDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.enums.InvalidEnumClassDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.enums.InvalidEnumDefaultValueDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.group.CyclicConfigGroupValuesDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.group.EmptyConfigGroupValuesDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.group.InvalidConfigGroupValuesDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.config.json.InvalidJsonGenericTypesDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.configType.InvalidConfigTypeDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.configType.MissingDefaultValueAttributeDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.configType.NonFieldTargetDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.configType.NonMatchingConfigResolverDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.configType.WrongRetentionDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig.CyclicDefaultValueConfigDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig.NonConfigDefaultValueConfigDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.defaultConfig.WrongTypeDefaultValueConfigDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.defaultRes.InvalidDefaultValueResIdDetector;
import com.ironsource.aura.aircon.lint.detector.configField.ConfigFieldReferenceDetector;
import com.ironsource.aura.aircon.lint.detector.configField.InvalidFieldTypeDetector;
import com.ironsource.aura.aircon.lint.detector.configField.MissingDefaultValueDetector;
import com.ironsource.aura.aircon.lint.detector.configField.MissingSourceDetector;
import com.ironsource.aura.aircon.lint.detector.configField.MultipleConfigAnnotationDetector;
import com.ironsource.aura.aircon.lint.detector.configField.MultipleConfigsForSameKeyDetector;
import com.ironsource.aura.aircon.lint.detector.configField.MultipleDefaultValueAttributesDetector;
import com.ironsource.aura.aircon.lint.detector.configField.NonConstFieldDetector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UQualifiedReferenceExpression;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class AirConUsageDetector
        extends AbstractDetector {

    final static List<Class<? extends IssueDetector>> DETECTORS = new ArrayList<Class<? extends IssueDetector>>() {{
        // region @FeatureRemoteConfig
        add(FeatureRemoteConfigOnClassDetector.class);
        add(DuplicateFeatureRemoteDetector.class);
        //endregion

        // region @EnumConfig
        add(InconsistentRemoteValueAnnotationsDetector.class);
        add(InvalidEnumClassDetector.class);
        add(InvalidEnumDefaultValueDetector.class);
        //endregion

        // region @JsonConfig
        add(InvalidJsonGenericTypesDetector.class);
        //endregion

        // region @ConfigGroup
        add(CyclicConfigGroupValuesDetector.class);
        add(EmptyConfigGroupValuesDetector.class);
        add(InvalidConfigGroupValuesDetector.class);
        //endregion

        // region @ConfigType
        add(MissingDefaultValueAttributeDetector.class);
        add(NonFieldTargetDetector.class);
        add(WrongRetentionDetector.class);
        // Temp disabling this check to allow custom config with Object raw type
        //	add(NonMatchingConfigResolverDetector.class);
        add(InvalidConfigTypeDetector.class);
        //endregion

        // region @DefaultConfig
        add(CyclicDefaultValueConfigDetector.class);
        add(NonConfigDefaultValueConfigDetector.class);
        add(WrongTypeDefaultValueConfigDetector.class);
        //endregion

        // region @DefaultRes
        add(InvalidDefaultValueResIdDetector.class);
        //endregion

        // region Config field
        add(ConfigFieldReferenceDetector.class);
        add(InvalidFieldTypeDetector.class);
        add(MissingDefaultValueDetector.class);
        add(MissingSourceDetector.class);
        add(MultipleConfigAnnotationDetector.class);
        add(MultipleConfigsForSameKeyDetector.class);
        add(MultipleDefaultValueAttributesDetector.class);
        add(NonConstFieldDetector.class);
        //endregion

        // region Aux methods
        add(AuxMethodInvalidSignatureDetector.class);
        add(NonConfigAuxMethodAnnotationValueDetector.class);
        add(ConfigValidatorInvalidSignature.class);
        add(ConfigMockProtectionDetector.class);
        //endregion

        // region General
        add(ConfigAttributeAnnotationOnNonConfigFieldDetector.class);
        //endregion
    }};

    private Visitor mVisitor;

    @Override
    protected UastVisitor getUastVisitor(final JavaContext context) {
        if (mVisitor == null) {
            mVisitor = createVisitor(context);
        }
        return mVisitor;
    }

    private Visitor createVisitor(final JavaContext context) {
        final Visitor visitor = new Visitor();

        registerDetectors(context, visitor);

        return visitor;
    }

    private void registerDetectors(final JavaContext context, final Visitor visitor) {
        for (Class<? extends IssueDetector> detectorClass : DETECTORS) {
            final IssueDetector detector = createDetector(detectorClass, context);
            if (detector != null) {
                visitor.registerIssueDetector(detector);
            }
        }
    }

    private IssueDetector createDetector(final Class<? extends IssueDetector> detectorClass, final JavaContext context) {
        try {
            return detectorClass.getConstructor(JavaContext.class)
                    .newInstance(context);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static class Visitor
            extends UastVisitor {

        private final List<IssueDetector> mIssueDetectors;

        private Visitor() {
            mIssueDetectors = new ArrayList<>();
        }

        private void registerIssueDetector(final IssueDetector issueDetector) {
            mIssueDetectors.add(issueDetector);
        }

        @Override
        public boolean visitClass(@Nonnull final UClass node) {
            for (IssueDetector issueDetector : mIssueDetectors) {
                issueDetector.visit(node);
            }
            return super.visitClass(node);
        }

        @Override
        public boolean visitField(@NotNull final UField node) {
            for (IssueDetector issueDetector : mIssueDetectors) {
                issueDetector.visit(node);
            }
            return super.visitField(node);
        }

        @Override
        public boolean visitMethod(@NotNull final UMethod node) {
            for (IssueDetector issueDetector : mIssueDetectors) {
                issueDetector.visit(node);
            }
            return super.visitMethod(node);
        }

        @Override
        public boolean visitAnnotation(@NotNull final UAnnotation node) {
            if (node.getJavaPsi() != null) {
                for (IssueDetector issueDetector : mIssueDetectors) {
                    issueDetector.visit(node);
                }
            }
            return super.visitAnnotation(node);
        }

        @Override
        public boolean visitCallExpression(@NotNull final UCallExpression node) {
            for (IssueDetector issueDetector : mIssueDetectors) {
                issueDetector.visit(node);
            }
            return super.visitCallExpression(node);
        }

        @Override
        public boolean visitQualifiedReferenceExpression(@NotNull final UQualifiedReferenceExpression node) {
            for (IssueDetector issueDetector : mIssueDetectors) {
                issueDetector.visit(node);
            }
            return super.visitQualifiedReferenceExpression(node);
        }

        @Override
        protected void onClassVisited(final UClass node) {
            for (IssueDetector issueDetector : mIssueDetectors) {
                issueDetector.onClassVisited(node);
            }
        }
    }
}
