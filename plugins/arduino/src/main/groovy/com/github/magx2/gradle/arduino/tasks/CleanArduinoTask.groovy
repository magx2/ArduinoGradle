package com.github.magx2.gradle.arduino.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

class CleanArduinoTask extends DefaultTask {

	@InputDirectory File arduinoMainBuildPath = "build/arduino" as File

	@TaskAction
	void clean() {
		if (arduinoMainBuildPath.exists()) {
			arduinoMainBuildPath.deleteDir()
		}

	}
}
