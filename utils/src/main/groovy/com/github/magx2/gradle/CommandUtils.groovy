package com.github.magx2.gradle

final class CommandUtils {
	private CommandUtils() {}

	static Map execute(String... cmd) {
		final processBuilder = new ProcessBuilder(cmd)
		processBuilder.redirectErrorStream(true)
		final process = processBuilder.start()
		process.waitFor()
		[
				exitValue: process.exitValue(),
				text: process.text
		]
	}
}
