package com.github.magx2.gradle.arduino

import com.github.magx2.gradle.arduino.tasks.CompileArduinoTask
import com.github.magx2.gradle.arduino.tasks.PrecompileArduinoTask
import com.github.magx2.gradle.arduino.tasks.UploadArduinoTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ArduinoPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.tasks.create("precompileArduino", PrecompileArduinoTask)

		project.tasks.create("uploadArduino", UploadArduinoTask)
		project.tasks.create("compileArduino", CompileArduinoTask)
	}
}
