package com.github.magx2.gradle.arduino.tasks

import com.github.magx2.gradle.CommandUtils
import com.github.magx2.gradle.FileUtils
import com.github.magx2.gradle.arduino.CommandErrorException
import com.github.magx2.gradle.arduino.IllegalArgumentException
import com.github.magx2.gradle.arduino.NotSetReferenceException
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.DefaultTask
import org.gradle.api.Nullable
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.util.regex.Pattern

import static com.github.magx2.gradle.OsUtils.windows

@PackageScope
abstract class ArduinoTask extends DefaultTask {
	private static final Pattern MAIN_ARDUINO_PATTERN = Pattern.compile('[\\\\/]([a-zA-Z0-9]+)\\.ino')

	@InputDirectory File arduinoDir = project.arduinoDir ? new File(project.arduinoDir as String) : null
	@InputDirectory File precompiledDir = project.tasks['precompileArduino']?.precompiledDir
	@InputDirectory File tmpDir = new File("$project.buildDir/arduino/tmp")
	/**
	 * Path to main sketch file. Should drop "**\/src/main/arduino" prefix.<br>
	 * Also it need to start with "/" (or "\") and end with ".ino"
	 */
	@Input String mainArduino

	@Input @Optional @Nullable String portName
	boolean verbose
	boolean verboseBuild
	boolean verboseUpload
	@Input @Optional @Nullable String board
	@Input @Nullable Map<String, String> preferences = [:]
	boolean savePreferences
	@Input @Optional @Nullable File preferencesFile

	@TaskAction
	@CompileStatic
	def runTask() {
		if (!arduinoDir) throw new NotSetReferenceException("arduinoDir")
		if (!precompiledDir) throw new NotSetReferenceException("precompiledDir")
		if (!tmpDir) throw new NotSetReferenceException("tmpDir")
		if (!mainArduino) throw new NotSetReferenceException("mainArduino")

		if (!MAIN_ARDUINO_PATTERN.matcher(mainArduino).matches()) throw new IllegalArgumentException("!${MAIN_ARDUINO_PATTERN.pattern()}.matches(\"$mainArduino\")")

		final mainArduinoFileName = findMainArduinoFileName();
		File finalProjectDir = new File(tmpDir, mainArduinoFileName)
		finalProjectDir.mkdirs()

		FileUtils.copyFromDirs(srcDir: precompiledDir, destDir: finalProjectDir)

		final mainArduinoFile = new File(finalProjectDir, mainArduino)
		final cmd = buildCmd(mainArduinoFile)

		logger.debug(" > Running command: ${cmd.join(" ")}")
		def output = CommandUtils.execute(cmd)
		if (!output.exitValue) {
			throw new CommandErrorException(output.exitValue as int, output.text as String, cmd)
		}
	}

	@CompileStatic
	String findMainArduinoFileName() {
		final matcher = MAIN_ARDUINO_PATTERN.matcher(mainArduino)
		if (matcher.matches()) {
			matcher.group(1)
		} else {
			throw new IllegalArgumentException("!${MAIN_ARDUINO_PATTERN.pattern()}.matches(\"$mainArduino\")")
		}
	}

	@CompileStatic
	private String[] buildCmd(File mainArduinoFile) {
		final cmd = [] as List<String>
		cmd << arduinoExecutable()
		cmd << option()
		if (portName) cmd << "--port" << portName
		if (board) cmd << "--board" << board
		if (verbose) cmd << "--verbose"
		if (verboseBuild) cmd << "--verbose-build"
		if (verboseUpload) cmd << "--verbose-upload"
		if (preferences) {
			preferences
					.collect { entry -> "$entry.key=$entry.value" }
					.each { pref -> cmd << ("--pref" as String) << (pref as String) }
		}
		if (savePreferences) cmd << "--save-prefs"
		if (preferencesFile) cmd << "--preferences-file" << preferencesFile.absolutePath
		cmd << mainArduinoFile.absolutePath

		cmd as String[]
	}

	@CompileStatic
	private String arduinoExecutable() {
		final exe = "$arduinoDir.absolutePath/arduino${windows ? ".exe" : ""}"
		logger.debug(" > Arduino executable: $exe")
		exe
	}

	@CompileStatic
	void addPreference(String key, String value) {
		logger.debug(" > Putting preference $key=$value")
		preferences.put(key, value)
	}

	protected abstract String option()
}
