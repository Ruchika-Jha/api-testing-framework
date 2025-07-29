package com.testlead.automation.clients;

import com.testlead.automation.base.BaseApiClient;
import com.testlead.automation.models.Product;
import io.restassured.response.Response;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * API client for Product operations
 */
public class ProductApiClient extends BaseApiClient {
    
    /**
     * Create a new product
     */
    public Response createProduct(Product product) {
        logger.info("Creating product with name: {}", product.getName());
        
        return given(getRequestSpec())
            .body(product)
            .when()
            .post("/products");
    }
    
    /**
     * Get product by ID
     */
    public Response getProductById(Long productId) {
        logger.info("Getting product by ID: {}", productId);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .when()
            .get("/products/{id}");
    }
    
    /**
     * Update product
     */
    public Response updateProduct(Long productId, Product product) {
        logger.info("Updating product with ID: {}", productId);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .body(product)
            .when()
            .put("/products/{id}");
    }
    
    /**
     * Delete product
     */
    public Response deleteProduct(Long productId) {
        logger.info("Deleting product with ID: {}", productId);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .when()
            .delete("/products/{id}");
    }
    
    /**
     * Get all products
     */
    public Response getAllProducts() {
        logger.info("Getting all products");
        
        return given(getRequestSpec())
            .when()
            .get("/products");
    }
    
    /**
     * Get products with pagination
     */
    public Response getProducts(int page, int size) {
        logger.info("Getting products - Page: {}, Size: {}", page, size);
        
        return given(getRequestSpec())
            .queryParam("page", page)
            .queryParam("size", size)
            .when()
            .get("/products");
    }
    
    /**
     * Bulk create products
     */
    public Response bulkCreateProducts(List<Product> products) {
        logger.info("Bulk creating {} products", products.size());
        
        return given(getRequestSpec())
            .body(products)
            .when()
            .post("/products/bulk");
    }
    
    /**
     * Get products by category
     */
    public Response getProductsByCategory(String category) {
        logger.info("Getting products by category: {}", category);
        
        return given(getRequestSpec())
            .queryParam("category", category)
            .when()
            .get("/products");
    }
    
    /**
     * Get products by price range
     */
    public Response getProductsByPriceRange(double minPrice, double maxPrice) {
        logger.info("Getting products by price range: {} - {}", minPrice, maxPrice);
        
        return given(getRequestSpec())
            .queryParam("minPrice", minPrice)
            .queryParam("maxPrice", maxPrice)
            .when()
            .get("/products");
    }
    
    /**
     * Search products by name
     */
    public Response searchProductsByName(String name) {
        logger.info("Searching products by name: {}", name);
        
        return given(getRequestSpec())
            .queryParam("name", name)
            .when()
            .get("/products/search");
    }
    
    /**
     * Get product inventory
     */
    public Response getProductInventory(Long productId) {
        logger.info("Getting inventory for product ID: {}", productId);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .when()
            .get("/products/{id}/inventory");
    }
    
    /**
     * Update product inventory
     */
    public Response updateProductInventory(Long productId, int quantity) {
        logger.info("Updating inventory for product ID: {} to quantity: {}", productId, quantity);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .body("{\"quantity\":" + quantity + "}")
            .when()
            .put("/products/{id}/inventory");
    }
    
    /**
     * Deactivate product
     */
    public Response deactivateProduct(Long productId) {
        logger.info("Deactivating product with ID: {}", productId);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .when()
            .put("/products/{id}/deactivate");
    }
    
    /**
     * Activate product
     */
    public Response activateProduct(Long productId) {
        logger.info("Activating product with ID: {}", productId);
        
        return given(getRequestSpec())
            .pathParam("id", productId)
            .when()
            .put("/products/{id}/activate");
    }
}