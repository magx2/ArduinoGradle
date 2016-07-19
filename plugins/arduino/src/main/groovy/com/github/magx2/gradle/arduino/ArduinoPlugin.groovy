package com.github.magx2.gradle.arduino

import com.github.magx2.gradle.arduino.tasks.CleanArduinoTask
import com.github.magx2.gradle.arduino.tasks.CompileArduinoTask
import com.github.magx2.gradle.arduino.tasks.PrecompileArduinoTask
import com.github.magx2.gradle.arduino.tasks.UploadArduinoTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ArduinoPlugin implements Plugin<Project> {
	private static final String TASKS_GROUP = 'arduino'

	@Override
	void apply(Project project) {
		final cleanArduino = project.tasks.create("cleanArduino", CleanArduinoTask)
		cleanArduino.group = TASKS_GROUP
		project.getTasksByName('clean', false)?.each { clean ->
			clean.dependsOn cleanArduino
		}

		final precompileArduino = project.tasks.create("precompileArduino", PrecompileArduinoTask)
		precompileArduino.group = TASKS_GROUP

		final uploadArduino = project.tasks.create("uploadArduino", UploadArduinoTask)
		final compileArduino = project.tasks.create("compileArduino", CompileArduinoTask)

		uploadArduino.group = TASKS_GROUP
		compileArduino.group = TASKS_GROUP

		uploadArduino.dependsOn precompileArduino
		compileArduino.dependsOn precompileArduino
	}
}
