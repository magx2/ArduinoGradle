package com.github.magx2.gradle.internal

import groovy.transform.CompileStatic

@CompileStatic
class ArduinoSerial {
	static final int BAUD = 9600;

	final boolean debug

	ArduinoSerial(boolean debug = false) {
		this.debug = debug
	}

	String initSerial(int baud = BAUD) {
		decorateInNotDebugMode("Serial.begin($baud);")
	}

	String print(String msg) {
		decorateInNotDebugMode("Serial.print(\"$msg\");")
	}

	String println(String msg) {
		decorateInNotDebugMode("Serial.println(\"$msg\");")
	}


	String read(String var, String defaultChar = '') {
		decorateInNotDebugMode("char $var = Serial.read();", "char $var = '$defaultChar'; // debug is ON")
	}

	private String decorateInNotDebugMode(String text) {
		decorateInNotDebugMode(text, "// $text // debug is OFF")
	}

	private String decorateInNotDebugMode(String notDebug, String debugS) {
		if (!debug) {
			debugS
		} else {
			notDebug
		}
	}
}
