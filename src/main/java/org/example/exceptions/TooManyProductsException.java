package org.example.exceptions;

public class TooManyProductsException extends Exception {

    public TooManyProductsException(String message) {
        super(message);
    }
}
