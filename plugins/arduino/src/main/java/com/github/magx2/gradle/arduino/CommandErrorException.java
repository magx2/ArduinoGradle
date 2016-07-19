package com.github.magx2.gradle.arduino;

import java.util.stream.Stream;

import static java.lang.String.format;

public class CommandErrorException extends ArduinoPluginException {
    public CommandErrorException(int exitValue, String outputText, String[] cmd) {
        super(format("Command returned exit value \"%s\" and it was not 0!%nOutput: %s%nCmd:%n%s",
                exitValue, outputText, processCmd(cmd)));
    }

    private static String processCmd(String[] cmd) {
        return Stream.of(cmd).reduce("", (acc, i) -> {
            if (i.startsWith("-")) {
                return format("%s%n    %s ", acc, i);
            } else {
                return format("%s%s", acc, i);
            }
        });
    }

}
