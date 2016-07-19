package com.github.magx2.gradle.arduino.tasks.templateengines

final class NoOpTemplateEngine implements TemplateEngine {
	private static final long serialVersionUID = 1L;

	@Override String precompile(String text, Map<String, String> context) { text }
}
