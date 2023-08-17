package com.ProductDetails.controller;


import com.ProductDetails.entity.ProductEntity;
import com.ProductDetails.entity.ProductQueueEntity;
import com.ProductDetails.exception.*;
import com.ProductDetails.model.Product;
import com.ProductDetails.model.SearchContext;
import com.ProductDetails.service.ProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductManager productManager;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveProduct() {
        List<ProductEntity> productList = productManager.getActiveProducts();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    // Use query parameter here
    @RequestMapping(value = "/products/search", method = RequestMethod.GET)
    public ResponseEntity<Object> getProduct(
            @RequestParam("productName") Optional<String> productName,
            @RequestParam("minPrice") Optional<Integer> minPrice,
            @RequestParam("maxPrice") Optional<Integer> maxPrice,
            @RequestParam("minPostedDate") Optional<String> minPostedDate,
            @RequestParam("maxPostedDate") Optional<String> maxPostedDate
            ) {

        SearchContext searchContext = new SearchContext();
        searchContext.setProductName(productName.orElse(""));
        searchContext.setMinPrice(minPrice.orElse(0));
        searchContext.setMaxPrice(maxPrice.orElse(10000));
        searchContext.setMinPostedDate(minPostedDate.orElse("2020-01-01"));
        searchContext.setMaxPostedDate(maxPostedDate.orElse("2030-01-01"));

        List<ProductEntity> productList = productManager.searchProducts(searchContext);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Object> createProducts(@RequestBody Product product) throws ProductPriceInvalidException, ProductStatusInvalidException, ProductNameInvalidException {
        boolean status = productManager.createProduct(product);
        return new ResponseEntity<>(status, status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/products/approval-queue", method = RequestMethod.GET)
    public ResponseEntity<Object> getToBeApprovedProduct() {
        List<ProductQueueEntity> productQueueEntities = productManager.getToBeApproveProducts();
        return new ResponseEntity<>(productQueueEntities, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/approval-queue/{id}/approve", method = RequestMethod.PUT)
    public ResponseEntity<Object> approveProduct(@PathVariable int id) throws ServerSideException, ResourceNotFoundException {
        String result = productManager.approveProduct(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/approval-queue/{id}/reject", method = RequestMethod.PUT)
    public ResponseEntity<Object> rejectProduct(@PathVariable int id) throws ResourceNotFoundException, ServerSideException {
        String result = productManager.rejectProduct(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateProduct(@RequestBody Product product , @PathVariable int id)
            throws ProductNameInvalidException, ProductStatusInvalidException, ProductPriceInvalidException {
        String responseMsg = productManager.updateProduct(product, id);
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteProduct(@PathVariable int id) {
        int result = productManager.deleteProduct(id);
        return new ResponseEntity<>("Delete successful", HttpStatus.OK);
    }
}
