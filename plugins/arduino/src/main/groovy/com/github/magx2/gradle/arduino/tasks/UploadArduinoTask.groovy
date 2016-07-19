package com.github.magx2.gradle.arduino.tasks

class UploadArduinoTask extends ArduinoTask {
	@Override protected String option() { "--upload" }
}
