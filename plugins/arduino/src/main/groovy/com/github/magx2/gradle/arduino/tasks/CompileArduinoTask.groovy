package com.github.magx2.gradle.arduino.tasks

class CompileArduinoTask extends ArduinoTask {
	@Override protected String option() { "--verify" }
}
