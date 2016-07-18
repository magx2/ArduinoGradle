package com.github.magx2.gradle.arduino.tasks

import com.github.magx2.gradle.FileUtils
import com.github.magx2.gradle.arduino.tasks.templateengines.TemplateEngine
import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class PrecompileArduinoTask extends DefaultTask {
	@InputDirectory File srcDir = new File("src/main/arduino")
	@OutputDirectory File precompiledDir = new File("$project.buildDir/arduiono/precompiled")
	@Input TemplateEngine templateEngine
	@Input Map<String, String> context = [:]

	@TaskAction
	def precompile() {
		srcDir.eachFileRecurse(FileType.FILES) { file ->
			final precompiledFile = FileUtils.createFileInDir(file: file, srcDir: srcDir, destDir: precompiledDir)
			precompiledFile.createNewFile()
			precompiledFile.withWriter { writer ->
				writer.write templateEngine.precompile(file, context)
			}
		}
	}
}
