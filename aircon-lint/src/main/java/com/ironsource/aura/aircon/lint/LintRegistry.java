package com.ironsource.aura.aircon.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;
import com.ironsource.aura.aircon.lint.detector.FeatureRemoteConfigOnClassDetector;
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
import com.ironsource.aura.aircon.lint.detector.remote.RemoteMethodCallDetector;
import com.ironsource.aura.aircon.lint.detector.remote.WrongConfigTypeDetector;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Contains references to all custom lint checks for config.
 */
public class LintRegistry
		extends IssueRegistry {

	@Nonnull
	@Override
	public List<Issue> getIssues() {
		final ArrayList<Issue> issues = new ArrayList<>();
		// region @FeatureRemoteConfig
		issues.add(FeatureRemoteConfigOnClassDetector.ISSUE);
		//endregion

		// region @EnumConfig
		issues.add(InconsistentRemoteValueAnnotationsDetector.ISSUE);
		issues.add(InvalidEnumClassDetector.ISSUE);
		issues.add(InvalidEnumDefaultValueDetector.ISSUE);
		//endregion

		// region @JsonConfig
		issues.add(InvalidJsonGenericTypesDetector.ISSUE);
		//endregion

		// region @ConfigGroup
		issues.add(CyclicConfigGroupValuesDetector.ISSUE);
		issues.add(EmptyConfigGroupValuesDetector.ISSUE);
		issues.add(InvalidConfigGroupValuesDetector.ISSUE);
		//endregion

		// region @DefaultConfig
		issues.add(CyclicDefaultValueConfigDetector.ISSUE);
		issues.add(NonConfigDefaultValueConfigDetector.ISSUE);
		issues.add(WrongTypeDefaultValueConfigDetector.ISSUE);
		//endregion

		// region @DefaultRes
		issues.add(InvalidDefaultValueResIdDetector.ISSUE);
		//endregion

		// region Config field
		issues.add(ConfigFieldReferenceDetector.ISSUE);
		issues.add(InvalidFieldTypeDetector.ISSUE);
		issues.add(MissingDefaultValueDetector.ISSUE);
		issues.add(MissingSourceDetector.ISSUE);
		issues.add(MultipleConfigAnnotationDetector.ISSUE);
		issues.add(MultipleConfigsForSameKeyDetector.ISSUE);
		issues.add(MultipleDefaultValueAttributesDetector.ISSUE);
		issues.add(NonConstFieldDetector.ISSUE);
		//endregion

		// region @Remote
		issues.add(RemoteMethodCallDetector.ISSUE);
		issues.add(WrongConfigTypeDetector.ISSUE);
		//endregion

		// region Aux methods
		issues.add(AuxMethodInvalidSignatureDetector.ISSUE);
		issues.add(NonConfigAuxMethodAnnotationValueDetector.ISSUE);
		issues.add(ConfigValidatorInvalidSignature.ISSUE);
		issues.add(ConfigMockProtectionDetector.ISSUE);
		//endregion

		// region General
		issues.add(ConfigAttributeAnnotationOnNonConfigFieldDetector.ISSUE);
		//endregion
		return issues;
	}

	@Override
	public int getApi() {
		return ApiKt.CURRENT_API;
	}
}
