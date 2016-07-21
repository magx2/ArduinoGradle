package com.github.magx2.gradle.arduino.tasks.templateengines

final class NoOpTemplateEngine implements TemplateEngine {
	@Override String precompile(String text, Map<String, Object> context) { text }
}
