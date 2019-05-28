package com.ironsource.aura.aircon.lint;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created on 11/8/2018.
 */
public abstract class AbstractDetector
		extends Detector
		implements Detector.UastScanner {

	@Override
	public List<Class<? extends UElement>> getApplicableUastTypes() {
		return Collections.<Class<? extends UElement>> singletonList(UClass.class);
	}

	@Override
	public UElementHandler createUastHandler(@Nonnull final JavaContext context) {
		return new UElementHandler() {
			@Override
			public void visitClass(@Nonnull UClass node) {
				final UastVisitor uastVisitor = getUastVisitor(context);
				node.accept(uastVisitor);
				uastVisitor.onClassVisited(node);
			}
		};
	}

	protected abstract UastVisitor getUastVisitor(final JavaContext context);

	protected static abstract class UastVisitor
			extends AbstractUastVisitor {

		protected void onClassVisited(UClass node) {
			// No-op, to be overridden
		}
	}
}
