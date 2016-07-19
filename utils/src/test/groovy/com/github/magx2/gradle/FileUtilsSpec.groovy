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

	def "should create file in new dir with changed name"() {
		given:
		final srcDir = new File("$BASIC_DIR/src_dir_123")
		final destDir = new File("$BASIC_DIR/dest_dir_123")

		srcDir.mkdirs()
		destDir.mkdirs()

		assert srcDir.isDirectory(): "srcDir need to be dir!"
		assert destDir.isDirectory(): "destDir need to be dir!"

		final file = new File(srcDir, "temp1.txt")
		file.createNewFile()

		when:
		final newFile = FileUtils.createFileInDir(file: file, srcDir: srcDir, destDir: destDir, doWithFileName: { fileName -> fileName.reverse()})

		then:
		newFile.absolutePath.endsWith("$BASIC_DIR${File.separator}dest_dir_123${File.separator}txt.1pmet")
		newFile.exists()
	}

	def "should throw exception when file is not in srcDir"() {
		given:
		final srcDir = new File("$BASIC_DIR/src_dir_2")
		srcDir.mkdirs()
		assert srcDir.isDirectory(): "srcDir need to be dir!"

		final file = new File("$BASIC_DIR/not_src_dir_2", "not_in_src_folder.txt")
		file.mkdirs()
		file.createNewFile()

		when:
		FileUtils.createFileInDir(file: file, srcDir: srcDir)

		then:
		Exception ex = thrown()
		ex.class == IllegalArgumentException
	}

	def "should copy files from one dir to another"() {
		given:
		final srcDir = "$BASIC_DIR/copy_from_dirs/src" as File
		final destDir = "$BASIC_DIR/copy_from_dirs/dest" as File

		srcDir.mkdirs()
		destDir.mkdirs()

		final fileText = "test test test"
		final file = "file.txt"
		new File(srcDir, file).with {
			createNewFile()
			write(fileText)
		}

		when:
		FileUtils.copyFromDirs(srcDir: srcDir, destDir: destDir)

		then:
		final outputFile = new File(destDir, file)
		outputFile.exists()
		outputFile.text == fileText
	}

	def "should copy files from one dir to another (dir of file is in subfolder)"() {
		given:
		final srcDir = "$BASIC_DIR/copy_from_dirs/src" as File
		final destDir = "$BASIC_DIR/copy_from_dirs/dest" as File

		srcDir.mkdirs()
		destDir.mkdirs()

		final subDir = "sub"
		new File(srcDir, subDir).mkdirs()

		final fileText = "test test test"
		final file = "$subDir/file.txt"
		new File(srcDir, file).with {
			createNewFile()
			write(fileText)
		}

		when:
		FileUtils.copyFromDirs(srcDir: srcDir, destDir: destDir)

		then:
		final outputFile = new File(destDir, file)
		outputFile.exists()
		outputFile.text == fileText
	}

	def "should thr NullPointerException when srcDir is null"() {
		given:
		final srcDir = null
		final destDir = "$BASIC_DIR/copy_from_dirs/dest" as File

		when:
		FileUtils.copyFromDirs(srcDir: srcDir, destDir: destDir)

		then:
		Exception ex = thrown()
		ex.class == NullPointerException
	}

	def "should thr NullPointerException when destDir is null"() {
		given:
		final srcDir = "$BASIC_DIR/copy_from_dirs/src" as File
		final destDir = null

		when:
		FileUtils.copyFromDirs(srcDir: srcDir, destDir: destDir)

		then:
		Exception ex = thrown()
		ex.class == NullPointerException
	}

	def "should thr IllegalArgumentException when srcDir does not exist"() {
		given:
		final srcDir = "$BASIC_DIR/copy_from_dirs/src_not_exists" as File
		final destDir = "$BASIC_DIR/copy_from_dirs/dest" as File

		destDir.mkdirs()

		when:
		FileUtils.copyFromDirs(srcDir: srcDir, destDir: destDir)

		then:
		Exception ex = thrown()
		ex.class == IllegalArgumentException
	}

	def "should thr IllegalArgumentException when destDir does not exist"() {
		given:
		final srcDir = "$BASIC_DIR/copy_from_dirs/src" as File
		final destDir = "$BASIC_DIR/copy_from_dirs/dest_not_exists" as File

		srcDir.mkdirs()

		when:
		FileUtils.copyFromDirs(srcDir: srcDir, destDir: destDir)

		then:
		Exception ex = thrown()
		ex.class == IllegalArgumentException
	}
}
