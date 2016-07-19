package com.github.magx2.gradle

import groovy.swing.SwingBuilder

final class CommandLineUtils {
	private CommandLineUtils() {}

	static String userInput(Map<String, ? extends Object> params = [:]) {
		try {
			def pass = ''

			new SwingBuilder().edt {
				dialog(modal: true, // Otherwise the build will continue running before you closed the dialog
						title: params.title, // Dialog title
						alwaysOnTop: params.containsKey('alwaysOnTop') ? params.alwaysOnTop : true, // pretty much what the name says
						resizable: params.containsKey('resizable') ? params.resizable : false, // Don't allow the user to resize the dialog
						locationRelativeTo: params.containsKey('locationRelativeTo') ? params.locationRelativeTo : null, // Place dialog in center of the screen
						pack: params.containsKey('pack') ? params.pack : true, // We need to pack the dialog (so it will take the size of it's children
						show: params.containsKey('show') ? params.show : true // Let's show it
				) {
					vbox { // Put everything below each other
						label(text: params.label.text)
						input = textField()
						button(defaultButton: true, text: params.button.text, actionPerformed: {
							pass = input.text // Set pass variable to value of input field
							dispose(); // Close dialog
						})
					}
				}
			}

			return pass
		} catch (Exception ignored) {
			return System.console().readLine("\nPlease enter key passphrase: ")
		}
	}
}
