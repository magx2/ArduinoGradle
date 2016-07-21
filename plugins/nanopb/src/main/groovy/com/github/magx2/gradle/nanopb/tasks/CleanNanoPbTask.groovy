package com.github.magx2.gradle.nanopb.tasks

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

class CleanNanoPbTask extends DefaultTask {
	@InputDirectory File mainDir = "$project.buildDir/nanopb" as File

	@CompileStatic
	@TaskAction
	def clean() {
		mainDir.deleteDir()
	}
}
