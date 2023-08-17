package com.ProductDetails.exception;

public class ProductPriceInvalidException extends Exception {
    private String msg;

    public ProductPriceInvalidException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
