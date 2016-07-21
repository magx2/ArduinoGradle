package com.github.magx2.gradle.utils.exceptions;

import static java.lang.String.format;

public class NotSetReferenceException extends ArduinoPluginException {
    public NotSetReferenceException(String referenceName) {
        super(format("Reference with name \"%s\" was not set", referenceName));
    }
}
