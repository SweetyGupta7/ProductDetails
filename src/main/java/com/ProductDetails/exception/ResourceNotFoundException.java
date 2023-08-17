package com.ProductDetails.exception;

public class ResourceNotFoundException extends Exception {
    private String msg;

    public ResourceNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
