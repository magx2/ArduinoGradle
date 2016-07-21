package com.github.magx2.gradle.arduino.tasks.templateengines

trait TemplateEngine {
	abstract String precompile(String text, Map<String, Object> context)

	String precompile(File file, Map<String, Object> context) {
		precompile(file.text, context)
	}
}