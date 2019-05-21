package com.ironsource.aura.aircon.compiler.utils;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

/**
 * Created on 10/27/2018.
 */
public abstract class SimpleProcessor
		extends AbstractProcessor {

	private ProcessingEnvironment mProcessingEnvironment;

	protected ProcessingUtils mProcessingUtils;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
		mProcessingEnvironment = processingEnvironment;
		mProcessingUtils = new ProcessingUtils(processingEnvironment);
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	protected void writeClassToFile(String packageName, TypeSpec typeSpec) {
		writeClassToFile(null, packageName, typeSpec);
	}

	protected void writeClassToFile(final Element element, String packageName, TypeSpec typeSpec) {
		try {
			JavaFile.builder(packageName, typeSpec)
			        .build()
			        .writeTo(mProcessingEnvironment.getFiler());
		} catch (IOException e) {
			if (element != null) {
				mProcessingUtils.getLogger()
				                .e(element, "Unable to write remote config api for class: %s", element.getSimpleName());
			}
		}
	}
}
