package com.testlead.automation.tests.integration;

import com.testlead.automation.base.BaseTest;
import com.testlead.automation.clients.ProductApiClient;
import com.testlead.automation.clients.UserApiClient;
import com.testlead.automation.models.Product;
import com.testlead.automation.models.User;
import com.testlead.automation.testdata.ProductTestData;
import com.testlead.automation.testdata.UserTestData;
import com.testlead.automation.utils.JsonUtils;
import com.testlead.automation.utils.ReportUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests for multiple API endpoints working together
 */
public class IntegrationTest extends BaseTest {
    
    private UserApiClient userApiClient;
    private ProductApiClient productApiClient;
    private User testUser;
    private List<Long> createdProductIds;
    private Long testUserId;
    
    @BeforeClass
    public void setupIntegrationTests() {
        userApiClient = new UserApiClient();
        productApiClient = new ProductApiClient();
        createdProductIds = new ArrayList<>();
        ReportUtils.logInfo("Integration test setup completed");
    }
    
    @Test(priority = 1, description = "End-to-end user and product workflow")
    public void testUserProductWorkflow() {
        ReportUtils.logInfo("Starting end-to-end user and product workflow test");
        
        try {
            // Step 1: Create a user
            testUser = UserTestData.getValidUser(); // Fixed: Use correct method name
            Response userResponse = userApiClient.createUser(testUser);
            
            Assert.assertEquals(userResponse.getStatusCode(), 201, "User creation should succeed");
            User createdUser = JsonUtils.fromJson(userResponse.getBody().asString(), User.class);
            testUserId = createdUser.getId();
            ReportUtils.logInfo("User created with ID: " + testUserId);
            
            // Step 2: Create multiple products
            List<Product> products = ProductTestData.getMultipleValidProducts(3); // Fixed: Use correct method
            for (Product product : products) {
                Response productResponse = productApiClient.createProduct(product);
                Assert.assertEquals(productResponse.getStatusCode(), 201, "Product creation should succeed");
                
                Product createdProduct = JsonUtils.fromJson(productResponse.getBody().asString(), Product.class);
                createdProductIds.add(createdProduct.getId());
                ReportUtils.logInfo("Product created with ID: " + createdProduct.getId());
            }
            
            // Step 3: Verify products are retrievable
            for (Long productId : createdProductIds) {
                Response getResponse = productApiClient.getProductById(productId);
                Assert.assertEquals(getResponse.getStatusCode(), 200, "Product should be retrievable");
            }
            
            // Step 4: Update user information
            User updatedUser = testUser.toBuilder()
                .firstName("Updated " + testUser.getFirstName())
                .lastName("Updated " + testUser.getLastName())
                .build();
            
            Response updateResponse = userApiClient.updateUser(testUserId, updatedUser);
            Assert.assertEquals(updateResponse.getStatusCode(), 200, "User update should succeed");
            
            ReportUtils.logPass("End-to-end user and product workflow completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("User product workflow failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 2, dependsOnMethods = "testUserProductWorkflow", description = "Bulk operations integration")
    public void testBulkOperationsIntegration() {
        ReportUtils.logInfo("Starting bulk operations integration test");
        
        try {
            // Step 1: Create multiple products in bulk
            List<Product> bulkProducts = ProductTestData.getMultipleValidProducts(5);
            Response bulkCreateResponse = productApiClient.bulkCreateProducts(bulkProducts);
            
            Assert.assertEquals(bulkCreateResponse.getStatusCode(), 201, "Bulk create should succeed");
            ReportUtils.logInfo("Bulk products created successfully");
            
            // Step 2: Get all products to verify bulk creation
            Response getAllResponse = productApiClient.getAllProducts();
            Assert.assertEquals(getAllResponse.getStatusCode(), 200, "Get all products should succeed");
            
            String responseBody = getAllResponse.getBody().asString();
            Assert.assertTrue(responseBody.contains("\"id\""), "Response should contain product data");
            
            // Step 3: Test pagination with the increased product count
            Response paginationResponse = productApiClient.getProducts(1, 10);
            Assert.assertEquals(paginationResponse.getStatusCode(), 200, "Pagination should work");
            
            ReportUtils.logPass("Bulk operations integration completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Bulk operations integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 3, dependsOnMethods = "testUserProductWorkflow", description = "Product filtering and search integration")
    public void testProductFilteringIntegration() {
        ReportUtils.logInfo("Starting product filtering integration test");
        
        try {
            // Step 1: Create products with specific categories
            String[] categories = {"Electronics", "Books", "Clothing"};
            for (String category : categories) {
                Product categoryProduct = ProductTestData.getProductWithCategory(category); // Fixed: Use correct method
                Response response = productApiClient.createProduct(categoryProduct);
                Assert.assertEquals(response.getStatusCode(), 201, "Category product creation should succeed");
                
                Product createdProduct = JsonUtils.fromJson(response.getBody().asString(), Product.class);
                createdProductIds.add(createdProduct.getId());
            }
            
            // Step 2: Test category filtering
            for (String category : categories) {
                Response categoryResponse = productApiClient.getProductsByCategory(category);
                Assert.assertEquals(categoryResponse.getStatusCode(), 200, "Category filtering should work");
                ReportUtils.logInfo("Category filtering tested for: " + category);
            }
            
            // Step 3: Test price range filtering
            Response priceRangeResponse = productApiClient.getProductsByPriceRange(10.0, 100.0);
            Assert.assertEquals(priceRangeResponse.getStatusCode(), 200, "Price range filtering should work");
            
            // Step 4: Test product search
            Response searchResponse = productApiClient.searchProductsByName("Test");
            Assert.assertEquals(searchResponse.getStatusCode(), 200, "Product search should work");
            
            ReportUtils.logPass("Product filtering integration completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Product filtering integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 4, dependsOnMethods = "testUserProductWorkflow", description = "Product inventory management integration")
    public void testInventoryManagementIntegration() {
        ReportUtils.logInfo("Starting inventory management integration test");
        
        try {
            if (!createdProductIds.isEmpty()) {
                Long productId = createdProductIds.get(0);
                
                // Step 1: Check initial inventory
                Response inventoryResponse = productApiClient.getProductInventory(productId);
                Assert.assertEquals(inventoryResponse.getStatusCode(), 200, "Get inventory should work");
                
                // Step 2: Update inventory
                int newQuantity = 150;
                Response updateInventoryResponse = productApiClient.updateProductInventory(productId, newQuantity);
                Assert.assertEquals(updateInventoryResponse.getStatusCode(), 200, "Update inventory should work");
                
                // Step 3: Verify inventory update
                Response verifyResponse = productApiClient.getProductInventory(productId);
                Assert.assertEquals(verifyResponse.getStatusCode(), 200, "Verify inventory should work");
                
                // Step 4: Test product activation/deactivation
                Response deactivateResponse = productApiClient.deactivateProduct(productId);
                Assert.assertEquals(deactivateResponse.getStatusCode(), 200, "Product deactivation should work");
                
                Response activateResponse = productApiClient.activateProduct(productId);
                Assert.assertEquals(activateResponse.getStatusCode(), 200, "Product activation should work");
                
                ReportUtils.logPass("Inventory management integration completed successfully");
            } else {
                ReportUtils.logWarning("No products available for inventory management test");
            }
            
        } catch (Exception e) {
            ReportUtils.logFail("Inventory management integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 5, description = "Error handling integration")
    public void testErrorHandlingIntegration() {
        ReportUtils.logInfo("Starting error handling integration test");
        
        try {
            // Step 1: Test user creation with invalid data
            User invalidUser = UserTestData.getUserWithInvalidEmail(); // Fixed: Use correct method
            Response invalidUserResponse = userApiClient.createUser(invalidUser);
            Assert.assertTrue(invalidUserResponse.getStatusCode() >= 400, "Invalid user should return error");
            
            // Step 2: Test product creation with invalid data
            Product invalidProduct = ProductTestData.getInvalidProduct(); // Fixed: Use correct method
            Response invalidProductResponse = productApiClient.createProduct(invalidProduct);
            Assert.assertTrue(invalidProductResponse.getStatusCode() >= 400, "Invalid product should return error");
            
            // Step 3: Test accessing non-existent resources
            Response nonExistentUser = userApiClient.getUserById(999999L);
            Assert.assertEquals(nonExistentUser.getStatusCode(), 404, "Non-existent user should return 404");
            
            Response nonExistentProduct = productApiClient.getProductById(999999L);
            Assert.assertEquals(nonExistentProduct.getStatusCode(), 404, "Non-existent product should return 404");
            
            ReportUtils.logPass("Error handling integration completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Error handling integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 6, description = "Performance and load integration")
    public void testPerformanceIntegration() {
        ReportUtils.logInfo("Starting performance integration test");
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Step 1: Create multiple users concurrently (simulated)
            for (int i = 0; i < 5; i++) {
                User user = UserTestData.getValidUser();
                Response response = userApiClient.createUser(user);
                // Check response time
                Assert.assertTrue(response.getTime() < 5000, "Response should be under 5 seconds");
            }
            
            // Step 2: Create multiple products concurrently (simulated)
            for (int i = 0; i < 5; i++) {
                Product product = ProductTestData.getValidProduct();
                Response response = productApiClient.createProduct(product);
                Assert.assertTrue(response.getTime() < 5000, "Response should be under 5 seconds");
            }
            
            // Step 3: Test pagination performance
            Response paginationResponse = productApiClient.getProducts(1, 50);
            Assert.assertEquals(paginationResponse.getStatusCode(), 200, "Large pagination should work");
            Assert.assertTrue(paginationResponse.getTime() < 10000, "Large pagination should be performant");
            
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            
            ReportUtils.logInfo("Total test execution time: " + totalTime + " ms");
            ReportUtils.logPass("Performance integration completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Performance integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 7, dependsOnMethods = "testUserProductWorkflow", description = "Data consistency integration")
    public void testDataConsistencyIntegration() {
        ReportUtils.logInfo("Starting data consistency integration test");
        
        try {
            if (!createdProductIds.isEmpty() && testUserId != null) {
                // Step 1: Verify user data consistency
                Response userResponse = userApiClient.getUserById(testUserId);
                Assert.assertEquals(userResponse.getStatusCode(), 200, "User should be retrievable");
                
                User retrievedUser = JsonUtils.fromJson(userResponse.getBody().asString(), User.class);
                Assert.assertNotNull(retrievedUser.getId(), "User ID should be consistent");
                
                // Step 2: Verify product data consistency
                for (Long productId : createdProductIds) {
                    Response productResponse = productApiClient.getProductById(productId);
                    Assert.assertEquals(productResponse.getStatusCode(), 200, "Product should be retrievable");
                    
                    Product retrievedProduct = JsonUtils.fromJson(productResponse.getBody().asString(), Product.class);
                    Assert.assertEquals(retrievedProduct.getId(), productId, "Product ID should be consistent");
                }
                
                // Step 3: Test data relationships (if applicable)
                // This would test relationships between users and products
                // For example, user favorite products, user orders, etc.
                
                ReportUtils.logPass("Data consistency integration completed successfully");
            } else {
                ReportUtils.logWarning("Insufficient test data for consistency check");
            }
            
        } catch (Exception e) {
            ReportUtils.logFail("Data consistency integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 8, dependsOnMethods = "testUserProductWorkflow", description = "Cleanup integration test")
    public void testCleanupIntegration() {
        ReportUtils.logInfo("Starting cleanup integration test");
        
        try {
            // Step 1: Delete created products
            for (Long productId : createdProductIds) {
                Response deleteResponse = productApiClient.deleteProduct(productId);
                // Some products might already be deleted in other tests
                Assert.assertTrue(deleteResponse.getStatusCode() == 204 || deleteResponse.getStatusCode() == 404, 
                    "Product deletion should succeed or product should already be deleted");
            }
            
            // Step 2: Delete created user
            if (testUserId != null) {
                Response deleteUserResponse = userApiClient.deleteUser(testUserId);
                Assert.assertEquals(deleteUserResponse.getStatusCode(), 204, "User deletion should succeed");
            }
            
            // Step 3: Verify cleanup
            if (testUserId != null) {
                Response verifyUserDeletion = userApiClient.getUserById(testUserId);
                Assert.assertEquals(verifyUserDeletion.getStatusCode(), 404, "Deleted user should not be found");
            }
            
            ReportUtils.logPass("Cleanup integration completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Cleanup integration failed: " + e.getMessage());
            // Don't throw on cleanup failure to avoid affecting other tests
        }
    }
    
    @Test(priority = 9, description = "Cross-service validation integration")
    public void testCrossServiceValidationIntegration() {
        ReportUtils.logInfo("Starting cross-service validation integration test");
        
        try {
            // Step 1: Create user and verify it can be used in product operations
            User serviceUser = UserTestData.getValidUser();
            Response userResponse = userApiClient.createUser(serviceUser);
            Assert.assertEquals(userResponse.getStatusCode(), 201, "Service user creation should succeed");
            
            User createdServiceUser = JsonUtils.fromJson(userResponse.getBody().asString(), User.class);
            
            // Step 2: Create product associated with user (if your API supports this)
            Product userProduct = ProductTestData.getValidProduct();
            // If your API supports creator/owner field:
            // userProduct.setCreatedBy(createdServiceUser.getId());
            
            Response productResponse = productApiClient.createProduct(userProduct);
            Assert.assertEquals(productResponse.getStatusCode(), 201, "User product creation should succeed");
            
            // Step 3: Verify cross-service data integrity
            Product createdProduct = JsonUtils.fromJson(productResponse.getBody().asString(), Product.class);
            createdProductIds.add(createdProduct.getId());
            
            // Verify the product can be retrieved
            Response verifyResponse = productApiClient.getProductById(createdProduct.getId());
            Assert.assertEquals(verifyResponse.getStatusCode(), 200, "Cross-service product should be retrievable");
            
            // Clean up service user
            userApiClient.deleteUser(createdServiceUser.getId());
            
            ReportUtils.logPass("Cross-service validation integration completed successfully");
            
        } catch (Exception e) {
            ReportUtils.logFail("Cross-service validation integration failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 10, description = "Concurrent operations integration test")
public void testConcurrentOperationsIntegration() {
    ReportUtils.logInfo("Starting concurrent operations integration test");
    
    try {
        List<Thread> threads = new ArrayList<>();
        List<Exception> threadExceptions = new ArrayList<>();
        
        // Create multiple threads for concurrent operations
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                try {
                    // Each thread creates a user and product
                    User threadUser = UserTestData.getValidUser();
                    threadUser.setUsername("concurrentUser" + threadId);
                    threadUser.setEmail("concurrent" + threadId + "@test.com");
                    
                    Response userResponse = userApiClient.createUser(threadUser);
                    if (userResponse.getStatusCode() != 201) {
                        throw new AssertionError("Concurrent user creation failed for thread " + threadId);
                    }
                    
                    Product threadProduct = ProductTestData.getValidProduct();
                    threadProduct.setName("ConcurrentProduct" + threadId);
                    
                    Response productResponse = productApiClient.createProduct(threadProduct);
                    if (productResponse.getStatusCode() != 201) {
                        throw new AssertionError("Concurrent product creation failed for thread " + threadId);
                    }
                    
                    ReportUtils.logInfo("Thread " + threadId + " completed successfully");
                    
                } catch (Exception e) {
                    synchronized (threadExceptions) {
                        threadExceptions.add(e);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join(10000); // Wait max 10 seconds per thread
        }
        
        // Check if any thread failed
        if (!threadExceptions.isEmpty()) {
            throw new AssertionError("Concurrent operations failed: " + threadExceptions.get(0).getMessage());
        }
        
        ReportUtils.logPass("Concurrent operations integration completed successfully");
        
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Restore interrupted status
        ReportUtils.logFail("Concurrent operations integration interrupted: " + e.getMessage());
        throw new RuntimeException("Test was interrupted", e);
    } catch (Exception e) {
        ReportUtils.logFail("Concurrent operations integration failed: " + e.getMessage());
        throw e;
    }
    }
}