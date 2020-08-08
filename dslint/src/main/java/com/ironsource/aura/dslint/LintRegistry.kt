package com.ironsource.aura.dslint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

/**
 * Contains references to all custom lint checks for config.
 */
class LintRegistry : IssueRegistry() {

    override val api = CURRENT_API

    override val issues = listOf(DslMandatoryDetector.ISSUE)
}