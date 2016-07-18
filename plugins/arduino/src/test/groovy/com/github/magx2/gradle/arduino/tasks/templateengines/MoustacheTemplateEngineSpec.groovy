package com.github.magx2.gradle.arduino.tasks.templateengines

import spock.lang.Specification

class MoustacheTemplateEngineSpec extends Specification {
	private final MoustacheTemplateEngine moustache = new MoustacheTemplateEngine()

	def "should parse simple template"() {
		given:
		final template = "My name is {{name}}, your name is {{yourName}}!"
		final context = [name: "Martin", yourName: "John"]

		when:
		final precompile = moustache.precompile(template, context)

		then:
		precompile == "My name is Martin, your name is John!"
	}

	def "should parse simple template (from file)"() {
		given:
		final templateFile = File.createTempFile("moustache", ".txt")
		templateFile.createNewFile()
		templateFile.withWriter { it.write("My name is {{name}}, your name is {{yourName}}!")}
		final context = [name: "Martin", yourName: "John"]

		when:
		final precompile = moustache.precompile(templateFile, context)

		then:
		precompile == "My name is Martin, your name is John!"
	}
}
