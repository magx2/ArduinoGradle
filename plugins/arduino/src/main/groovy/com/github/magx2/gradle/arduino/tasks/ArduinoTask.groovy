package com.github.magx2.gradle.arduino.tasks

import com.github.magx2.gradle.CommandUtils
import com.github.magx2.gradle.FileUtils
import com.github.magx2.gradle.utils.exceptions.CommandErrorException
import com.github.magx2.gradle.utils.exceptions.NotSetReferenceException
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.DefaultTask
import org.gradle.api.Nullable
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.util.regex.Pattern

import static com.github.magx2.gradle.OsUtils.windows

@PackageScope
abstract class ArduinoTask extends DefaultTask {
	private static final Pattern MAIN_ARDUINO_PATTERN = Pattern.compile('([a-zA-Z0-9_-]+)\\.ino')

	@InputDirectory
	File arduinoDir = project.arduinoDir ? new File(project.arduinoDir as String) : null
	@OutputDirectory
	File precompiledDir = project.tasks['precompileArduino']?.precompiledDir
	@OutputDirectory
	File tmpDir = new File("$project.buildDir/arduino/tmp")
	/**
	 * Path to main sketch file. Should drop "**\/src/main/arduino" prefix.<br>
	 * Also it need to start with "/" (or "\") and end with ".ino"
	 */
	@InputFile
	File mainArduino

	boolean verbose
	boolean verboseBuild
	@Input
	@Nullable
	Map<String, String> preferences = [:]
	boolean savePreferences
	@Input
	@Optional
	@Nullable
	File preferencesFile
	boolean preserveTempFiles

	ArduinoTask() {
		arduinoDir?.mkdirs()
		precompiledDir?.mkdirs()
		tmpDir?.mkdirs()
	}

	void setMainArduino(String mainArduino) {
		this.mainArduino = new File("$project.projectDir/src/main/arduino", mainArduino)
	}

	void setMainArduino(File mainArduino) {
		this.mainArduino = mainArduino
	}

	@TaskAction
	@CompileStatic
	def runTask() {
		if (!arduinoDir) throw new NotSetReferenceException("arduinoDir")
		if (!precompiledDir) throw new NotSetReferenceException("precompiledDir")
		if (!tmpDir) throw new NotSetReferenceException("tmpDir")
		if (!mainArduino) throw new NotSetReferenceException("mainArduino")

		if (!MAIN_ARDUINO_PATTERN.matcher(mainArduino.name).matches()) throw new IllegalArgumentException("!${MAIN_ARDUINO_PATTERN.pattern()}.matches(\"$mainArduino.name\")")

		final mainArduinoFileName = findMainArduinoFileName();
		File finalProjectDir = new File(tmpDir, mainArduinoFileName)
		finalProjectDir.mkdirs()

		FileUtils.copyFromDirs(srcDir: precompiledDir, destDir: finalProjectDir)

		final mainArduinoFile = new File(finalProjectDir, mainArduino.name)
		final cmd = buildCmd(mainArduinoFile)

		logger.debug(" > Running command: ${cmd.join(" ")}")
		def output
		try {
			output = CommandUtils.execute({ logger.info it?.toString() }, { logger.error it?.toString() }, cmd)
		} catch (Exception e) {
			throw new CommandErrorException(cmd, e)
		}
		if (output) {
			throw new CommandErrorException(output, cmd)
		}
	}

	@CompileStatic
	String findMainArduinoFileName() {
		final matcher = MAIN_ARDUINO_PATTERN.matcher(mainArduino.name)
		if (matcher.matches()) {
			matcher.group(1)
		} else {
			throw new IllegalArgumentException("!${MAIN_ARDUINO_PATTERN.pattern()}.matches(\"$mainArduino.name\")")
		}
	}

	@CompileStatic
	private String[] buildCmd(File mainArduinoFile) {
		final cmd = [] as List<String>
		cmd << arduinoExecutable()
		cmd << option()
		if (verbose) cmd << "--verbose"
		if (verboseBuild) cmd << "--verbose-build"
		if (preferences) {
			preferences
					.collect { entry -> "$entry.key=$entry.value" }
					.each { pref -> cmd << ("--pref" as String) << (pref as String) }
		}
		if (savePreferences) cmd << "--save-prefs"
		if (preferencesFile) cmd << "--preferences-file" << preferencesFile.absolutePath
		if (preserveTempFiles) cmd << "--preserve-temp-files"

		cmd.addAll(ownCommands())

		cmd << mainArduinoFile.absolutePath

		cmd as String[]
	}

	@CompileStatic
	private String arduinoExecutable() {
		final exe = "$arduinoDir.absolutePath/arduino${windows ? "_debug.exe" : ""}"
		logger.debug(" > Arduino executable: $exe")
		exe
	}

	@CompileStatic
	void putPreference(Map<String, String> map = [:]) {
		preferences.putAll(map)
	}

	protected abstract String option()

	protected List<String> ownCommands() { [] }

}
