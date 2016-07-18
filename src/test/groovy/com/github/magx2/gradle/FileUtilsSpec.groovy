package com.github.magx2.gradle

import spock.lang.Specification

class FileUtilsSpec extends Specification {
	private static final BASIC_DIR = "build${File.separator}file_utils_spec"

	def "should create file in new dir (simple)"() {
		given:
		srcDir.mkdirs()
		destDir.mkdirs()

		assert srcDir.isDirectory(): "srcDir need to be dir!"
		assert destDir.isDirectory(): "destDir need to be dir!"

		final file = new File("temp1.txt", srcDir as File)
		file.createNewFile()

		when:
		final newFile = FileUtils.createFileInDir(file: file, srcDir: srcDir, destDir: destDir)

		then:
		newFile.absolutePath.endsWith(shouldEndWith)
		newFile.exists()

		where:
		srcDir << [
				new File("$BASIC_DIR/src_dir_1"),
				new File("$BASIC_DIR/src_dir_1/sub"),
				new File("$BASIC_DIR/src_dir_1/")
		]
		destDir << [
				new File("$BASIC_DIR/dest_dir_1"),
				new File("$BASIC_DIR/dest_dir_1"),
				new File("$BASIC_DIR/dest_dir_1/sub")
		]
		shouldEndWith << [
				"$BASIC_DIR${File.separator}dest_dir_1${File.separator}temp1.txt",
				"$BASIC_DIR${File.separator}dest_dir_1${File.separator}temp1.txt",
				"$BASIC_DIR${File.separator}dest_dir_1${File.separator}sub${File.separator}temp1.txt"
		]
	}

	def "should throw exception when file is not in srcDir"() {
		given:
		final srcDir = new File("$BASIC_DIR/src_dir_2")
		srcDir.mkdirs()
		assert srcDir.isDirectory(): "srcDir need to be dir!"

		final file = new File("not_in_src_folder.txt", "$BASIC_DIR/not_src_dir_2")
		file.mkdirs()
		file.createNewFile()

		when:
		FileUtils.createFileInDir(file: file, srcDir: srcDir)

		then:
		Exception ex = thrown()
		ex.class == IllegalArgumentException
	}
}
