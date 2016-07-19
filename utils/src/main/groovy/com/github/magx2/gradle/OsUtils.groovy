package com.github.magx2.gradle

import groovy.transform.CompileStatic

@CompileStatic
final class OsUtils {
	private OsUtils() {}

	static enum Os {
		Windows, Linux
	}

	static Os findOs() {
		final String osName = (System.properties['os.name'] as String).toLowerCase() ?: ""
		if (osName.contains('windows')) {
			Os.Windows
		} else if (osName.contains("linux")) {
			Os.Linux
		} else {
			throw new IllegalStateException("Doesn't know this OS \"$osName\"!")
		}
	}

	static boolean isWindows() {
		findOs() == Os.Windows
	}

	static boolean isLinux() {
		findOs() == Os.Linux
	}
}
