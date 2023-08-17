package com.ProductDetails.exception;

public class NoSuchProductExistsException extends RuntimeException {

    private String message;

    public NoSuchProductExistsException() {
    }

    public NoSuchProductExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}