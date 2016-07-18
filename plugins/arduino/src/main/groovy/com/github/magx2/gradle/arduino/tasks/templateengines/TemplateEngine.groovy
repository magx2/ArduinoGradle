package com.github.magx2.gradle.arduino.tasks.templateengines

trait TemplateEngine {
	abstract String precompile(String text, Map<String, String> context)

	String precompile(File file, Map<String, String> context) {
		precompile(file.text, context)
	}
}