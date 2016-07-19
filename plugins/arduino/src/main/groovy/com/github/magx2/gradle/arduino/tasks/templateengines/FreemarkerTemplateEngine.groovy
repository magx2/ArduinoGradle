package com.github.magx2.gradle.arduino.tasks.templateengines

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import freemarker.template.Version
import groovy.transform.CompileStatic

class FreemarkerTemplateEngine implements TemplateEngine {
	private final Configuration configuration

	FreemarkerTemplateEngine(Configuration configuration) {
		this.configuration = configuration
	}

	FreemarkerTemplateEngine() {
		this(createConfiguration())
	}

	@Override String precompile(String text, Map<String, String> context) {
		Template t = new Template("templateName", new StringReader(text), configuration);
		Writer out = new StringWriter()
		t.process(context, out)
		out.flush()
		out.toString()
	}

	@CompileStatic
	public static final Configuration createConfiguration() {
		Configuration configuration = new Configuration()

		configuration.setIncompatibleImprovements(new Version(2, 3, 20));
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLocale(Locale.US);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		configuration
	}
}
