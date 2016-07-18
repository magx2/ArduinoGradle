package com.github.magx2.gradle.arduino.tasks.templateengines

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory

class MoustacheTemplateEngine implements TemplateEngine {
	final MustacheFactory mustacheFactory = new DefaultMustacheFactory();

	@Override String precompile(String text, Map<String, String> context) {
		final mustache = mustacheFactory.compile(new StringReader(text), "temp1")
		final writer = new StringWriter()
		mustache.execute(writer, context)
		writer.flush()
		writer.toString()
	}
}
