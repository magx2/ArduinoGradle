package com.github.magx2.gradle.nanopb

import com.github.magx2.gradle.nanopb.tasks.CleanNanoPbTask
import com.github.magx2.gradle.nanopb.tasks.CompileNanoPbTask
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

class NanoPbPlugin implements Plugin<Project> {
	private static final String TASKS_GROUP = 'nanopb'

	@Override
	@CompileStatic
	void apply(Project project) {
		cleanNanoPb(project)
		compileNanoPb(project)
	}

	@CompileStatic
	private static void compileNanoPb(Project project) {
		final compileNanoPb = project.tasks.create("compileNanoPb", CompileNanoPbTask)
		compileNanoPb.group = TASKS_GROUP
	}

	@CompileStatic
	private static void cleanNanoPb(Project project) {
		final cleanArduino = project.tasks.create("cleanNanoPb", CleanNanoPbTask)
		cleanArduino.group = TASKS_GROUP
		project.getTasksByName('clean', false)?.each { clean ->
			clean.dependsOn cleanArduino
		}
	}
}
