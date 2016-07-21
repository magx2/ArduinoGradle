package com.github.magx2.gradle.nanopb

import com.github.magx2.gradle.nanopb.tasks.CleanNanoPbTask
import com.github.magx2.gradle.nanopb.tasks.NanoPbCompileTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class NanoPbPlugin implements Plugin<Project> {
	private static final String TASKS_GROUP = 'nanopb'

	@Override
	void apply(Project project) {
		final cleanArduino = project.tasks.create("cleanNanoPb", CleanNanoPbTask)
		cleanArduino.group = TASKS_GROUP
		project.getTasksByName('clean', false)?.each { clean ->
			clean.dependsOn cleanArduino
		}

		final compileNanoPb = project.tasks.create("compileNanoPb", NanoPbCompileTask)
		compileNanoPb.group = TASKS_GROUP
	}
}
