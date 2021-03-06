package com.github.magx2.gradle.utils.exceptions;

import static java.lang.String.format;

public class IllegalArgumentException extends ArduinoPluginException {
    public IllegalArgumentException(String expression) {
        super(format("Expression \"%s\" was false!", expression));
    }
}
