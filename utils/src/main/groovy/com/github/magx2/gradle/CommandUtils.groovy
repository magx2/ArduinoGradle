package com.github.magx2.gradle

import groovy.transform.CompileStatic

final class CommandUtils {
	private CommandUtils() {}

	@CompileStatic
	static int execute(Closure logger, Closure error, String... cmd) {
		def process = cmd.execute()

		process.in.eachLine { line -> logger line }
		process.err.eachLine { line -> error line }
		process.out.close()

		process.waitFor()
		process.exitValue()
	}
}
