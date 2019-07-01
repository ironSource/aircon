package com.ironsource.aura.aircon.lint.fix;

import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;

import org.jetbrains.uast.UElement;

/**
 * Created on 6/22/2019.
 */
public class IssueFixBuilder <T extends UElement> {

	private final JavaContext mContext;

	protected final T                    mTarget;
	protected final LintFix.GroupBuilder mGroupBuilder;

	public IssueFixBuilder(JavaContext context, T target, String name) {
		mContext = context;
		mTarget = target;
		mGroupBuilder = LintFix.create()
		                       .composite()
		                       .name(name);
	}

	public void replacePattern(String pattern, String replacement) {
		mGroupBuilder.add(newReplace().pattern(pattern)
		                              .with(replacement)
		                              .build());
	}

	public void replace(String original, String replacement) {
		mGroupBuilder.add(newReplace().text(original)
		                              .with(replacement)
		                              .build());
	}

	private LintFix.ReplaceStringBuilder newReplace() {
		return LintFix.create()
		              .replace()
		              .range(mContext.getLocation(mTarget))
		              .shortenNames()
		              .reformat(true);
	}

	public LintFix build() {
		return mGroupBuilder.build();
	}
}
