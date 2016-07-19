package com.github.magx2.gradle.arduino.tasks

import com.github.magx2.gradle.CommandLineUtils
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class UploadArduinoTask extends ArduinoTask {
	boolean verboseUpload
	@Input @Optional String board
	@Input @Optional String portName

	@Override protected String option() { "--upload" }

	@Override
	protected List<String> ownCommands() {
		final cmd = []

		if (!portName) {
			portName = CommandLineUtils.userInput(
					title: "Please pass portName",
					label: [text: "Please pass portName"],
					button: [text: "OK"]
			)
		}

		if (portName) cmd << "--port" << portName
		if (board) cmd << "--board" << board
		if (verboseUpload) cmd << "--verbose-upload"

		cmd
	}
}
