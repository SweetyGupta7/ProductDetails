package com.ProductDetails.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductAlreadyExistsException extends RuntimeException{
    private String msg;
    public ProductAlreadyExistsException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
