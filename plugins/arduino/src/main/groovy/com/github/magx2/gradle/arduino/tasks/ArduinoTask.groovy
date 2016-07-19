package com.github.magx2.gradle.arduino.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class ArduinoTask extends DefaultTask {
	@InputDirectory File arduinoDir = new File(project.arduinoDir as String)
	@InputDirectory File precompiledDir = project.tasks['precompileArduino']?.precompiledDir
	@InputDirectory File tmpDir = new File("$project.buildDir/arduino/tmp")
	@Input File mainArduinoFile // need to be set by user

	@Input String portName
	boolean verbose
	@Input String board
	@Input Map<String, String> preferences = [:]
	boolean savePreferences

	@TaskAction
	def runTask() {

	}
}
