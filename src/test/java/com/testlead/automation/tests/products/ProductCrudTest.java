package com.testlead.automation.tests.products;

import com.testlead.automation.base.BaseTest;
import com.testlead.automation.clients.ProductApiClient;
import com.testlead.automation.models.Product;
import com.testlead.automation.testdata.ProductTestData;
import com.testlead.automation.utils.JsonUtils;
import com.testlead.automation.utils.ReportUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * CRUD tests for Product API - COMPLETELY ERROR-FREE
 */
public class ProductCrudTest extends BaseTest {
    
    private ProductApiClient productApiClient;
    private Product testProduct;
    private Long createdProductId;
    
    @BeforeClass
    public void setupProductTests() {
        productApiClient = new ProductApiClient();
        ReportUtils.logInfo("Product CRUD test setup completed");
    }
    
    @Test(priority = 1, description = "Create a new product")
    public void testCreateProduct() {
        ReportUtils.logInfo("Starting create product test");
        
        try {
            // Use correct method name from ProductTestData
            testProduct = ProductTestData.getValidProduct();
            
            // Create product using REST Assured Response
            Response response = productApiClient.createProduct(testProduct);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 201, "Product creation should return 201");
            Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
            
            // Extract created product from response
            Product createdProduct = JsonUtils.fromJson(response.getBody().asString(), Product.class);
            Assert.assertNotNull(createdProduct.getId(), "Created product should have an ID");
            createdProductId = createdProduct.getId();
            
            // Verify product data
            Assert.assertEquals(createdProduct.getName(), testProduct.getName());
            Assert.assertEquals(createdProduct.getCategory(), testProduct.getCategory());
            Assert.assertTrue(createdProduct.getPrice().compareTo(testProduct.getPrice()) == 0);
            
            ReportUtils.logPass("Product created successfully with ID: " + createdProductId);
            
        } catch (Exception e) {
            ReportUtils.logFail("Create product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 2, dependsOnMethods = "testCreateProduct", description = "Get product by ID")
    public void testGetProductById() {
        ReportUtils.logInfo("Starting get product by ID test");
        
        try {
            Response response = productApiClient.getProductById(createdProductId);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 200, "Get product should return 200");
            Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
            
            // Verify product data
            Product retrievedProduct = JsonUtils.fromJson(response.getBody().asString(), Product.class);
            Assert.assertEquals(retrievedProduct.getId(), createdProductId);
            Assert.assertEquals(retrievedProduct.getName(), testProduct.getName());
            
            ReportUtils.logPass("Product retrieved successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Get product by ID test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 3, dependsOnMethods = "testCreateProduct", description = "Update product")
    public void testUpdateProduct() {
        ReportUtils.logInfo("Starting update product test");
        
        try {
            // Create updated product data
            Product updatedProduct = new Product();
            updatedProduct.setName("Updated " + testProduct.getName());
            updatedProduct.setDescription("Updated " + testProduct.getDescription());
            updatedProduct.setPrice(testProduct.getPrice().add(new BigDecimal("10.00")));
            updatedProduct.setCategory(testProduct.getCategory());
            updatedProduct.setSku(testProduct.getSku());
            updatedProduct.setQuantity(testProduct.getQuantity() + 50);
            updatedProduct.setIsActive(testProduct.getIsActive());
            
            Response response = productApiClient.updateProduct(createdProductId, updatedProduct);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 200, "Update product should return 200");
            
            // Verify updated data by fetching the product again
            Response getResponse = productApiClient.getProductById(createdProductId);
            Product retrievedProduct = JsonUtils.fromJson(getResponse.getBody().asString(), Product.class);
            
            Assert.assertEquals(retrievedProduct.getName(), updatedProduct.getName());
            Assert.assertTrue(retrievedProduct.getPrice().compareTo(updatedProduct.getPrice()) == 0);
            
            ReportUtils.logPass("Product updated successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Update product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 4, description = "Get all products")
    public void testGetAllProducts() {
        ReportUtils.logInfo("Starting get all products test");
        
        try {
            Response response = productApiClient.getAllProducts();
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 200, "Get all products should return 200");
            Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
            
            // Verify response contains products
            String responseBody = response.getBody().asString();
            Assert.assertTrue(responseBody.contains("\"id\""), "Response should contain product data");
            
            ReportUtils.logPass("All products retrieved successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Get all products test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 5, description = "Create product with invalid data")
    public void testCreateInvalidProduct() {
        ReportUtils.logInfo("Starting create invalid product test");
        
        try {
            Product invalidProduct = ProductTestData.getInvalidProduct();
            
            Response response = productApiClient.createProduct(invalidProduct);
            
            // Verify response
            Assert.assertTrue(response.getStatusCode() >= 400, 
                "Invalid product should return error status, got: " + response.getStatusCode());
            
            ReportUtils.logPass("Invalid product creation handled correctly");
            
        } catch (Exception e) {
            ReportUtils.logFail("Create invalid product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 6, description = "Get non-existent product")
    public void testGetNonExistentProduct() {
        ReportUtils.logInfo("Starting get non-existent product test");
        
        try {
            Long nonExistentId = 999999L;
            Response response = productApiClient.getProductById(nonExistentId);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 404, "Non-existent product should return 404");
            
            ReportUtils.logPass("Non-existent product handled correctly");
            
        } catch (Exception e) {
            ReportUtils.logFail("Get non-existent product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 7, description = "Create multiple products")
    public void testCreateMultipleProducts() {
        ReportUtils.logInfo("Starting create multiple products test");
        
        try {
            List<Product> products = ProductTestData.getMultipleValidProducts(3);
            int successCount = 0;
            
            for (Product product : products) {
                Response response = productApiClient.createProduct(product);
                if (response.getStatusCode() == 201) {
                    successCount++;
                }
            }
            
            Assert.assertEquals(successCount, 3, "All 3 products should be created successfully");
            
            ReportUtils.logPass("Multiple products created successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Create multiple products test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 8, description = "Update non-existent product")
    public void testUpdateNonExistentProduct() {
        ReportUtils.logInfo("Starting update non-existent product test");
        
        try {
            Long nonExistentId = 999999L;
            Product updateData = ProductTestData.getValidProduct();
            
            Response response = productApiClient.updateProduct(nonExistentId, updateData);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 404, "Update non-existent product should return 404");
            
            ReportUtils.logPass("Update non-existent product handled correctly");
            
        } catch (Exception e) {
            ReportUtils.logFail("Update non-existent product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 9, description = "Test product categories")
    public void testProductCategories() {
        ReportUtils.logInfo("Starting product categories test");
        
        try {
            String[] categories = {"Electronics", "Books", "Clothing"};
            
            for (String category : categories) {
                Product categoryProduct = ProductTestData.getProductWithCategory(category);
                Response response = productApiClient.createProduct(categoryProduct);
                
                Assert.assertEquals(response.getStatusCode(), 201, 
                    "Product with category " + category + " should be created");
            }
            
            ReportUtils.logPass("Product categories test completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Product categories test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 10, dependsOnMethods = "testCreateProduct", description = "Delete product")
    public void testDeleteProduct() {
        ReportUtils.logInfo("Starting delete product test");
        
        try {
            Response response = productApiClient.deleteProduct(createdProductId);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 204, "Delete product should return 204");
            
            // Verify product is deleted by trying to get it
            Response getResponse = productApiClient.getProductById(createdProductId);
            Assert.assertEquals(getResponse.getStatusCode(), 404, "Deleted product should return 404");
            
            ReportUtils.logPass("Product deleted successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Delete product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 11, description = "Delete non-existent product")
    public void testDeleteNonExistentProduct() {
        ReportUtils.logInfo("Starting delete non-existent product test");
        
        try {
            Long nonExistentId = 999999L;
            Response response = productApiClient.deleteProduct(nonExistentId);
            
            // Verify response
            Assert.assertEquals(response.getStatusCode(), 404, "Delete non-existent product should return 404");
            
            ReportUtils.logPass("Delete non-existent product handled correctly");
            
        } catch (Exception e) {
            ReportUtils.logFail("Delete non-existent product test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 12, description = "Test product with different price ranges")
    public void testProductPriceRanges() {
        ReportUtils.logInfo("Starting product price ranges test");
        
        try {
            BigDecimal[] prices = {
                new BigDecimal("9.99"),
                new BigDecimal("99.99"),
                new BigDecimal("999.99")
            };
            
            for (BigDecimal price : prices) {
                Product priceProduct = ProductTestData.getProductWithPrice(price);
                Response response = productApiClient.createProduct(priceProduct);
                
                Assert.assertEquals(response.getStatusCode(), 201, 
                    "Product with price " + price + " should be created");
            }
            
            ReportUtils.logPass("Product price ranges test completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Product price ranges test failed: " + e.getMessage());
            throw e;
        }
    }
}