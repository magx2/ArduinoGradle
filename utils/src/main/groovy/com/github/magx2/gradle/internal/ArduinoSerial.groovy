package com.github.magx2.gradle.internal

import groovy.transform.CompileStatic

@CompileStatic
class ArduinoSerial {
	static final int BAUD = 9600;

	final boolean debug

	ArduinoSerial(boolean debug = false) {
		this.debug = debug
	}

	@CompileStatic
	String initSerial(int baud = BAUD) {
		decorateInNotDebugMode(["Serial.begin($baud);".toString()])
	}

	@CompileStatic
	String echo(List<String> msgs) {
		final msg = msgs.collect { "Serial.print($it);".toString() }
		decorateInNotDebugMode(msg)
	}

	@CompileStatic
	String echoln(List<String> msgs) {
		final msg = msgs.collect { "Serial.print($it);".toString() }
		msg.add("Serial.println(\"\");")
		decorateInNotDebugMode(msg)
	}

	@CompileStatic
	String print(String... msgs) { echo(msgs.toList()) }

	@CompileStatic
	String println(String... msgs) { echoln(msgs.toList()) }

	@CompileStatic
	String prints(String... msgs) { echo(msgs.collect { "\"$it\"".toString() }) }

	@CompileStatic
	String printlns(String... msgs) { echoln(msgs.collect { "\"$it\"".toString() }) }

	@CompileStatic
	String read(String var, String defaultChar = '') {
		decorateInNotDebugMode("char $var = Serial.read();", "char $var = '$defaultChar'; // debug is ON")
	}

	@CompileStatic
	private String decorateInNotDebugMode(List<String> text) {
		final debug = text.collect {"// $it // debug is OFF"}.join("\n")
		decorateInNotDebugMode(text.join("\n"), debug)
	}

	@CompileStatic
	private String decorateInNotDebugMode(String notDebug, String debugS) {
		if (!debug) {
			debugS
		} else {
			notDebug
		}
	}
}
