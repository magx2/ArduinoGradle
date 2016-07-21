package com.github.magx2.gradle.nanopb.tasks

import com.github.magx2.gradle.utils.exceptions.NotSetReferenceException
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanNanopbTask extends DefaultTask {
	File mainDir = "$project.buildDir/nanopb" as File

	@CompileStatic
	@TaskAction
	def clean() {
		if(!mainDir) throw new NotSetReferenceException("mainDir")
		if(mainDir.exists()) mainDir.deleteDir()
	}
}
