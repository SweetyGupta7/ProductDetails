package com.ProductDetails.exception;

public class ProductNameInvalidException extends Exception {
    private String msg;

    public ProductNameInvalidException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
