package com.textmaster.core.exceptions;

/**
 * Used when a general error must be thrown.
 */
public class TextMasterException extends Exception {

    public TextMasterException() {
        super();
    }

    public TextMasterException(String message) {
        super(message);
    }
}
