package com.github.magx2.gradle.nanopb.tasks

import com.github.magx2.gradle.CommandUtils
import com.github.magx2.gradle.utils.exceptions.IllegalArgumentException
import com.github.magx2.gradle.utils.exceptions.NotSetReferenceException
import groovy.io.FileType
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.util.regex.Pattern

class CompileNanopbTask extends DefaultTask {
	@OutputDirectory
	File compileDir
	@InputDirectory
	File nanopbBin = project.hasProperty('nanopbBin') ? project.nanopbBin as File : null
	@Input
	@Optional
	List<File> protos
	String protoFolder = "src/main/proto"
	File defaultProtosDir = protoFolder as File

	CompileNanopbTask() {
		final mainDir = project.tasks['cleanNanopb']?.mainDir
		if (mainDir) {
			compileDir = new File((File) mainDir, "compiled")
		}
	}

	@TaskAction
	@CompileStatic
	def compileNanoPb() {
		if (!nanopbBin) throw new NotSetReferenceException("nanopbBin")
		if (!compileDir) throw new NotSetReferenceException("compileDir")

		if (!protos) {
			addProtosFromDefaultPath()
		}
		if (!protos) throw new NotSetReferenceException("protos")


		final cmd = []
		cmd << "$nanopbBin.absolutePath/protoc"
		cmd << "--nanopb_out=$compileDir.absolutePath"
//		cmd << "-I$defaultProtosDir.absolutePath"
		cmd.addAll(prepareImportsForProto() )
		cmd.addAll(protos.collect(this.&prepareProtoToCmdLine))


		logger.debug("cmd: ${cmd.join(" ")}")
		final execute = CommandUtils.execute(cmd as String[])
		logger.info("Nanopb-protoc: $execute.text")
	}

	private List<GString> prepareImportsForProto() {
		protos
				.collect { it.parentFile }
				.collect { it.absolutePath }
				.collect { "-I$it" }
	}

	@CompileStatic
	String prepareProtoToCmdLine(File proto) {
		final bareProtoPattern = ".*?${protoFolder.replaceAll("\\\\", "/")}.(.+)\$"
		final matcher = Pattern.compile(bareProtoPattern).matcher(proto.absolutePath.replaceAll("\\\\", "/"))
		if (matcher.matches()) {
			matcher.group(0)
		} else {
			throw new IllegalArgumentException("$proto.absolutePath do not match \"$bareProtoPattern\"")
		}
	}

	@CompileStatic
	private void addProtosFromDefaultPath() {
		protos = []
		logger.info("Protos list is empty, searching for protos in \"$defaultProtosDir.absolutePath\"")
		if (!defaultProtosDir) throw new NotSetReferenceException("defaultProtosDir")
		defaultProtosDir.eachFileRecurse(FileType.FILES) { file ->
			if (file.name.endsWith(".proto")) {
				logger.debug("Adding file \"$file.absolutePath\"")
				protos << file
			}
		}
	}

	void setNanopbBin(File nanopbBin) {
		this.nanopbBin = nanopbBin
	}

	void setNanopbBin(String nanopbBin) {
		setNanopbBin(nanopbBin as File)
	}
}
