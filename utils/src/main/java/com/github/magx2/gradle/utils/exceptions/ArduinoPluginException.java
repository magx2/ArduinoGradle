package com.github.magx2.gradle.utils.exceptions;

import org.gradle.api.GradleException;

public class ArduinoPluginException extends GradleException {

    public ArduinoPluginException(String message) {
        super(message);
    }

    public ArduinoPluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
