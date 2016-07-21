package com.github.magx2.gradle.arduino.tasks.templateengines

import groovy.text.GStringTemplateEngine

class GroovyTemplateEngine implements TemplateEngine {
	private GStringTemplateEngine engine

	GroovyTemplateEngine(GStringTemplateEngine engine) {
		this.engine = engine
	}

	GroovyTemplateEngine() {
		this(new GStringTemplateEngine())
	}

	@Override String precompile(String text, Map<String, String> context) {
		engine.createTemplate(text).make(context)
	}
}
