package com.ironsource.aura.aircon.compiler.utils;

import com.google.auto.common.Visibility;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created on 10/27/2018.
 */
public class ProcessingUtils {

	private ProcessingEnvironment mProcessingEnvironment;

	private final Logger mLogger;

	public ProcessingUtils(final ProcessingEnvironment processingEnvironment) {
		mProcessingEnvironment = processingEnvironment;
		mLogger = new Logger(processingEnvironment);
	}

	public ProcessingEnvironment getProcessingEnvironment() {
		return mProcessingEnvironment;
	}

	public Types getTypeUtils() {
		return mProcessingEnvironment.getTypeUtils();
	}

	public Elements getElementUtils() {
		return mProcessingEnvironment.getElementUtils();
	}

	public Logger getLogger() {
		return mLogger;
	}

	public TypeMirror unboxedType(TypeMirror typeMirror) {
		try {
			return getTypeUtils().unboxedType(typeMirror);
		} catch (IllegalArgumentException e) {
			return typeMirror;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Element> List<T> getEnclosedElementsByType(TypeElement element, Class<T> clazz) {
		final List<? extends Element> enclosedElements = element.getEnclosedElements();
		final List<T> relevantElements = new ArrayList<>();
		for (Element enclosedElement : enclosedElements) {
			if (clazz.isInstance(enclosedElement)) {
				relevantElements.add((T) enclosedElement);
			}
		}
		return relevantElements;
	}

	public static Modifier getVisibilityModifier(Element element) {
		switch (Visibility.ofElement(element)) {
			case PRIVATE:
				return Modifier.PRIVATE;
			case PROTECTED:
				return Modifier.PROTECTED;
			case PUBLIC:
				return Modifier.PUBLIC;
		}

		return null;
	}

	public static class Logger {

		private ProcessingEnvironment mEnv;

		private Logger(ProcessingEnvironment processingEnvironment) {
			mEnv = processingEnvironment;
		}

		public void e(Element element, String message, Object... args) {
			printMessage(Diagnostic.Kind.ERROR, element, message, args);
		}

		public void d(Element element, String message, Object... args) {
			printMessage(Diagnostic.Kind.NOTE, element, message, args);
		}

		private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
			if (args.length > 0) {
				message = String.format(message, args);
			}

			mEnv.getMessager()
			    .printMessage(kind, message, element);
		}
	}
}
