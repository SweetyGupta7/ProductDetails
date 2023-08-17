package com.ProductDetails.service;

import com.ProductDetails.exception.ProductNameInvalidException;
import com.ProductDetails.exception.ProductPriceInvalidException;
import com.ProductDetails.exception.ProductStatusInvalidException;
import com.ProductDetails.model.Product;

public class ServiceUtil {

    public static boolean validateUpdateProductRequest(Product product) throws ProductNameInvalidException, ProductStatusInvalidException, ProductPriceInvalidException {
        if (isBlankString(product.getName())) {
            throw new ProductNameInvalidException("Found empty or invalid product name");
        }

        if (isBlankString(product.getStatus())) {
            throw new ProductStatusInvalidException("Found empty or invalid product status");
        }

        if (product.getPrice() <= 0 || product.getPrice() > 10000) {
            throw new ProductPriceInvalidException("Price must be between 0 & 10000");
        }
        return true;
    }

    private static boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }

}
