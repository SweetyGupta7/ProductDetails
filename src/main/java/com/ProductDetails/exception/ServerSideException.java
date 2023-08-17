package com.ProductDetails.exception;

public class ServerSideException extends Exception {
    private String msg;

    public ServerSideException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
