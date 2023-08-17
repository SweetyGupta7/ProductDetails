package com.ProductDetails.service;


import com.ProductDetails.dao.ProductDao;
import com.ProductDetails.entity.ProductEntity;
import com.ProductDetails.entity.ProductQueueEntity;
import com.ProductDetails.exception.*;
import com.ProductDetails.model.Product;
import com.ProductDetails.model.SearchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ProductManager {

    @Autowired
    ProductDao productDao;

    public boolean createProduct(Product product) throws ProductPriceInvalidException, ProductStatusInvalidException, ProductNameInvalidException {
        ServiceUtil.validateUpdateProductRequest(product);

        ProductEntity productEntity = new ProductEntity();
        ProductQueueEntity productQueueEntity = new ProductQueueEntity();
        if (product.getPrice() < 10000) {
            if (product.getPrice() < 5000) {
                productEntity.setName(product.getName());
                productEntity.setPrice(product.getPrice());
                productEntity.setStatus(product.getStatus());
                productDao.createProduct(productEntity);
            } else {
                productQueueEntity.setName(product.getName());
                productQueueEntity.setPrice(product.getPrice());
                productQueueEntity.setStatus("pending");
                productQueueEntity.setRequest_type("create_product");
                productDao.createProductQueue(productQueueEntity);

            }
        } else {
            System.out.println("Price is not in valid range:" + product.getPrice());
            return false;
        }
        return true;
    }

    // List Active Products:
    public List<ProductEntity> getActiveProducts() {
        return productDao.getActiveProducts();
    }

    public List<ProductEntity> getProducts(Product product) {
        return productDao.getProducts(product);
    }

    //search product based on requestParam
    public List<ProductEntity> searchProducts(SearchContext searchContext) {
        System.out.println(searchContext);
        return productDao.getProductsByQuery(searchContext);
    }

    //Get all the products from the approval queue
    public List<ProductQueueEntity> getToBeApproveProducts() {
        return productDao.getToBeApproveProducts();
    }

    //Update an existing product
    public String updateProduct(Product product, int id) throws ProductNameInvalidException, ProductStatusInvalidException, ProductPriceInvalidException {
        ServiceUtil.validateUpdateProductRequest(product);

        ProductEntity productEntity = productDao.getProductById(id);
        if (productEntity == null) {
            throw new NoSuchProductExistsException(String.format("Product not found with the given id: %d", id));
        }

        if (product.getPrice() > 2 * productEntity.getPrice()) {
            ProductQueueEntity productQueueEntity = new ProductQueueEntity();
            productQueueEntity.setProduct_id(productEntity.getId());
            productQueueEntity.setName(product.getName());
            productQueueEntity.setStatus("pending");
            productQueueEntity.setPrice(product.getPrice());
            productQueueEntity.setRequest_type("update_product");
            productDao.createProductQueue(productQueueEntity);
            return "Update request has been sent to approval queue";
        } else {
            productDao.updateProducts(product, id);
            return "Update has been updated";
        }
    }

    //Delete product and pushed to the approval queue.
    public int deleteProduct(int id) {
        return productDao.deleteProduct(id);
    }

    //Get all the approved products
    public String approveProduct(int id) throws ServerSideException, ResourceNotFoundException {
        ProductQueueEntity productQueueEntity = productDao.getProductQueueEntityById(id);
        if (productQueueEntity == null) {
            throw new ResourceNotFoundException("approval request id is not valid");
        } else if (productQueueEntity.getRequest_type().equalsIgnoreCase("create_product")) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(productQueueEntity.getName());
            productEntity.setPrice(productQueueEntity.getPrice());
            productEntity.setStatus("active");
            productEntity.setCreatedAt(new Date());
            productEntity.setUpdatedAt(new Date());
            productDao.createProduct(productEntity);
        } else if (productQueueEntity.getRequest_type().equalsIgnoreCase("update_product")) {
            Product product = new Product();
            product.setName(productQueueEntity.getName());
            product.setPrice(productQueueEntity.getPrice());
            product.setStatus("active");
            productDao.updateProducts(product, productQueueEntity.getProduct_id());
        } else if (!productQueueEntity.getRequest_type().equalsIgnoreCase("delete_product")) {
            throw new ServerSideException("Server Error");
        }

        productDao.updateProductQueueEntity("approved", productQueueEntity.getId());

        return "Approval request is successful";
    }

    //Reject a product
    public String rejectProduct(int id) throws ResourceNotFoundException, ServerSideException {
        ProductQueueEntity productQueueEntity = productDao.getProductQueueEntityById(id);
        if (productQueueEntity == null) {
            throw new ResourceNotFoundException("approval request id is not valid");
        } else if (productQueueEntity.getRequest_type().equalsIgnoreCase("delete_product")) {
            Product product = new Product();
            product.setName(productQueueEntity.getName());
            product.setPrice(productQueueEntity.getPrice());
            product.setStatus("active");
            productDao.updateProducts(product, productQueueEntity.getProduct_id());
        } else if (!productQueueEntity.getRequest_type().equalsIgnoreCase("create_product") &&
                !productQueueEntity.getRequest_type().equalsIgnoreCase("update_product")) {
            throw new ServerSideException("Server Error");
        }

        productDao.updateProductQueueEntity("rejected", productQueueEntity.getId());

        return "Reject request is successful";
    }

}
