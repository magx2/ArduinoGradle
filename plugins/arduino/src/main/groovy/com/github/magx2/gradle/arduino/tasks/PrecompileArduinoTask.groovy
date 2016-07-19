package com.github.magx2.gradle.arduino.tasks

import com.github.magx2.gradle.FileUtils
import com.github.magx2.gradle.arduino.tasks.templateengines.FreemarkerTemplateEngine
import com.github.magx2.gradle.arduino.tasks.templateengines.MoustacheTemplateEngine
import com.github.magx2.gradle.arduino.tasks.templateengines.NoOpTemplateEngine
import com.github.magx2.gradle.arduino.tasks.templateengines.TemplateEngine
import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class PrecompileArduinoTask extends DefaultTask {
	public static final TemplateEngine NO_OP_TEMPLATE_ENGINE = new NoOpTemplateEngine()
	public static final TemplateEngine MOUSTACHE_TEMPLATE_ENGINE = new MoustacheTemplateEngine()
	public static final TemplateEngine FREEMARKER_TEMPLATE_ENGINE = new FreemarkerTemplateEngine()

	@InputDirectory File srcDir = new File("src/main/arduino")
	@OutputDirectory File precompiledDir = new File("$project.buildDir/arduiono/precompiled")
	@Input TemplateEngine templateEngine = NO_OP_TEMPLATE_ENGINE
	@Input Map<String, String> context = [:]

	@TaskAction
	def precompile() {
		srcDir.eachFileRecurse(FileType.FILES) { file ->
			logger.debug(" > Precompiling file: $file.absolutePath")
			final precompiledFile = FileUtils.createFileInDir(file: file, srcDir: srcDir, destDir: precompiledDir)
			precompiledFile.createNewFile()
			precompiledFile.withWriter { writer ->
				writer.write templateEngine.precompile(file, context)
			}
		}
	}
}
