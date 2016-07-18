package com.github.magx2.gradle

import groovy.transform.CompileStatic

final class FileUtils {
	private FileUtils() {}

	@CompileStatic
	static File createFileInDir(Map<String, File> params = [:]) {
		final filePath = params.file.absolutePath
		if (!filePath.startsWith(params.srcDir.absolutePath)) throw new IllegalArgumentException("File [$filePath] is not in srcDir [$params.srcDir.absolutePath]!")
		final onlyFileNamePath = filePath.replaceAll(params.srcDir.absolutePath.replaceAll('\\\\', "\\\\\\\\"), "")
		final newFile = (params.destDir.absolutePath + onlyFileNamePath) as File
		newFile.createNewFile()
		newFile
	}
}
