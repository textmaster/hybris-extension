package com.textmaster.core.exceptions;

/**
 * Used when an item is found whereas this element try to be created.
 */
public class ItemAlreadyExistsException extends Exception {

    public ItemAlreadyExistsException() {
        super();
    }

    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
