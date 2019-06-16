package com.ironsource.aura.aircon.lint;

import com.android.tools.lint.detector.api.JavaContext;
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
import com.ironsource.aura.aircon.lint.detector.annotation.configType.MissingDefaultValueAttributeDetector;
import com.ironsource.aura.aircon.lint.detector.annotation.configType.NonFieldTargetDetector;
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

/*
	TODO:
*/
public class AirConUsageDetector
		extends AbstractDetector {

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

		// region @FeatureRemoteConfig
		visitor.registerIssueDetector(new FeatureRemoteConfigOnClassDetector(context));
		//endregion

		// region @EnumConfig
		visitor.registerIssueDetector(new InconsistentRemoteValueAnnotationsDetector(context));
		visitor.registerIssueDetector(new InvalidEnumClassDetector(context));
		visitor.registerIssueDetector(new InvalidEnumDefaultValueDetector(context));
		//endregion

		// region @JsonConfig
		visitor.registerIssueDetector(new InvalidJsonGenericTypesDetector(context));
		//endregion

		// region @ConfigGroup
		visitor.registerIssueDetector(new CyclicConfigGroupValuesDetector(context));
		visitor.registerIssueDetector(new EmptyConfigGroupValuesDetector(context));
		visitor.registerIssueDetector(new InvalidConfigGroupValuesDetector(context));
		//endregion

		// region @ConfigType
		visitor.registerIssueDetector(new MissingDefaultValueAttributeDetector(context));
		visitor.registerIssueDetector(new NonFieldTargetDetector(context));
		//endregion

		// region @DefaultConfig
		visitor.registerIssueDetector(new CyclicDefaultValueConfigDetector(context));
		visitor.registerIssueDetector(new NonConfigDefaultValueConfigDetector(context));
		visitor.registerIssueDetector(new WrongTypeDefaultValueConfigDetector(context));
		//endregion

		// region @DefaultRes
		visitor.registerIssueDetector(new InvalidDefaultValueResIdDetector(context));
		//endregion

		// region Config field
		visitor.registerIssueDetector(new ConfigFieldReferenceDetector(context));
		visitor.registerIssueDetector(new InvalidFieldTypeDetector(context));
		visitor.registerIssueDetector(new MissingDefaultValueDetector(context));
		visitor.registerIssueDetector(new MissingSourceDetector(context));
		visitor.registerIssueDetector(new MultipleConfigAnnotationDetector(context));
		visitor.registerIssueDetector(new MultipleConfigsForSameKeyDetector(context));
		visitor.registerIssueDetector(new MultipleDefaultValueAttributesDetector(context));
		visitor.registerIssueDetector(new NonConstFieldDetector(context));
		//endregion

		// region Aux methods
		visitor.registerIssueDetector(new AuxMethodInvalidSignatureDetector(context));
		visitor.registerIssueDetector(new NonConfigAuxMethodAnnotationValueDetector(context));
		visitor.registerIssueDetector(new ConfigValidatorInvalidSignature(context));
		visitor.registerIssueDetector(new ConfigMockProtectionDetector(context));
		//endregion

		// region General
		visitor.registerIssueDetector(new ConfigAttributeAnnotationOnNonConfigFieldDetector(context));
		//endregion

		return visitor;
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
