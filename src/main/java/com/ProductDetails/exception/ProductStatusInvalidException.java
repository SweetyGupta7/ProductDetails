package com.ProductDetails.exception;

public class ProductStatusInvalidException extends Exception {
    private String msg;

    public ProductStatusInvalidException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
