package com.ironsource.aura.aircon.lint.detector.configField;

import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.ironsource.aura.aircon.lint.utils.ConfigElementsUtils;
import com.ironsource.aura.aircon.lint.utils.ElementUtils;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UMethod;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 21/1/19.
 */
public class MissingDefaultValueDetector
		extends DefaultValueIssueDetector {

	public static final Issue ISSUE = createErrorIssue("MissingDefaultValue", "Missing default value", "Config field must define defaultValue attribute **OR** annotated with @DefaultConfig/@DefaultRes **OR** define a @ConfigDefaultValueProvider method");

	public MissingDefaultValueDetector(final JavaContext context) {
		super(context, ISSUE);
	}

	private Set<PsiField> mConfigElements                = new HashSet<>();
	private Set<PsiField> mConfigFieldsWithDefaultValues = new HashSet<>();

	@Override
	protected void visitConfigField(final UField node, final boolean defaultValue, final boolean defaultConfig, final boolean defaultRes) {
		if (ConfigElementsUtils.isConfigGroupField(node.getPsi())) {
			return;
		}
		mConfigElements.add(node);
		if (defaultValue || defaultConfig || defaultRes) {
			mConfigFieldsWithDefaultValues.add(node);
		}
	}

	@Override
	public void visit(final UMethod node) {
		final UAnnotation defaultValueProviderAnnotation = ConfigElementsUtils.getDefaultValueProviderAnnotation(node);
		if (defaultValueProviderAnnotation == null) {
			return;
		}

		final PsiElement referencedConfig = defaultValueProviderAnnotation.findAttributeValue(null)
		                                                                  .getJavaPsi();
		final PsiField referencedField = ElementUtils.getReferencedField(referencedConfig);

		mConfigFieldsWithDefaultValues.add(referencedField);
	}

	@Override
	public void onClassVisited(final UClass node) {
		if (ConfigElementsUtils.hasFeatureRemoteConfigAnnotation(node)) {
			for (PsiField configElement : mConfigElements) {
				if (!containsConfig(configElement.getName(), mConfigFieldsWithDefaultValues)) {
					reportPsi(configElement);
				}
			}

			mConfigFieldsWithDefaultValues.clear();
			mConfigElements.clear();
		}
	}

	private boolean containsConfig(String name, Collection<PsiField> configs) {
		for (PsiField config : configs) {
			if (config.getName()
			          .equals(name)) {
				return true;
			}
		}
		return false;
	}
}
