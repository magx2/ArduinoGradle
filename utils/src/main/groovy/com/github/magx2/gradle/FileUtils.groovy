package com.github.magx2.gradle

import groovy.io.FileType

final class FileUtils {
	private FileUtils() {}

	static File createFileInDir(Map<String, Object> params = [:]) {
		if (!params.doWithFileName) params.doWithFileName = {fileName -> fileName}
		final filePath = params.file.absolutePath
		if (!filePath.startsWith(params.srcDir.absolutePath)) throw new IllegalArgumentException("File [$filePath] is not in srcDir [$params.srcDir.absolutePath]!")
		final onlyFileNamePath = filePath.replaceAll(params.srcDir.absolutePath.replaceAll('\\\\', "\\\\\\\\"), "").substring(1)
		final newFile = (params.destDir.absolutePath + File.separator + params.doWithFileName(onlyFileNamePath)) as File
		newFile.with {
			parentFile.mkdirs()
			createNewFile()
		}
		newFile
	}

	static void copyFromDirs(Map<String, File> params = [:]) {
		if(!params.srcDir.exists()) throw new IllegalArgumentException("SrdDir \"$params.srcDir.absolutePath\" does not exists!")
		if(!params.destDir.exists()) throw new IllegalArgumentException("DestDir \"$params.destDir.absolutePath\" does not exists!")

		params.srcDir.eachFileRecurse(FileType.FILES) { file ->
			createFileInDir(file: file, srcDir: params.srcDir, destDir: params.destDir).with {
				createNewFile()
				write(file.text)
			}
		}
	}
}
