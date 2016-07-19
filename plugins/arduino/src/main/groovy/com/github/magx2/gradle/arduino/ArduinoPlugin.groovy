package com.github.magx2.gradle.arduino

import com.github.magx2.gradle.arduino.tasks.CompileArduinoTask
import com.github.magx2.gradle.arduino.tasks.PrecompileArduinoTask
import com.github.magx2.gradle.arduino.tasks.UploadArduinoTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ArduinoPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		final precompileArduino = project.tasks.create("precompileArduino", PrecompileArduinoTask)

		final uploadArduino = project.tasks.create("uploadArduino", UploadArduinoTask)
		final compileArduino = project.tasks.create("compileArduino", CompileArduinoTask)

		uploadArduino.dependsOn precompileArduino
		compileArduino.dependsOn precompileArduino
	}
}
