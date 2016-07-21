package com.github.magx2.gradle.internal

import groovy.transform.CompileStatic

class Nanopb {
	private final File path
	private final File nanopbDir

	Nanopb(project) {
		path = "$project.parent.projectDir/bin/nanopb" as File
		nanopbDir = project.parent.tasks['compileNanopb'].compileDir
	}

	@CompileStatic
	String include(String... protos) {
		"""\
extern "C" {
${common()}

${encode()}

${decode()}

${includeProtos(protos).join("\n\n")}
}
"""
	}

	@CompileStatic
	String common() {
		"#include <$path.absolutePath/pb_common.h>\n" +
				"#include <$path.absolutePath/pb_common.c>"
	}

	@CompileStatic
	String encode() {
		"#include <$path.absolutePath/pb_encode.h>\n" +
				"#include <$path.absolutePath/pb_encode.c>"
	}

	@CompileStatic
	String decode() {
		"#include <$path.absolutePath/pb_decode.h>\n" +
				"#include <$path.absolutePath/pb_decode.c>"
	}

	@CompileStatic
	List<String> includeProtos(String... protos) {
		final includes = []

		for (String protoName : protos) {
			includes << "#include <$nanopbDir.absolutePath/src/main/proto/${protoName}.pb.h>\n" +
					"#include <$nanopbDir.absolutePath/src/main/proto/${protoName}.pb.c>"
		}

		includes
	}
}