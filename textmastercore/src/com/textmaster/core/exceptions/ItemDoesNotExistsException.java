package com.textmaster.core.exceptions;

/**
 * Used when an item try to be used whereas this element does not exist yet.
 */
public class ItemDoesNotExistsException extends Exception {

    public ItemDoesNotExistsException() {
        super();
    }

    public ItemDoesNotExistsException(String message) {
        super(message);
    }
}
